package org.trax.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.trax.dao.AwardConfigDao;
import org.trax.dao.BadgeConfigDao;
import org.trax.dao.BaseUnitTypeDao;
import org.trax.dao.OrganizationDao;
import org.trax.dao.RankConfigDao;
import org.trax.dao.RequirementConfigDao;
import org.trax.dao.UserDao;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.Badge;
import org.trax.model.BadgeConfig;
import org.trax.model.CourseConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.Organization;
import org.trax.model.Rank;
import org.trax.model.RankConfig;
import org.trax.model.Requirement;
import org.trax.model.RequirementConfig;
import org.trax.model.Scout;
import org.trax.model.Unit;
import org.trax.model.User;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.PinConfig;
import org.trax.model.cub.pu2015.ChildAwardConfig;
import org.trax.model.cub.pu2015.Cub2015RankConfig;
import org.trax.model.cub.pu2015.Cub2015RankElectiveConfig;
import org.trax.util.CSVReader;

@Service("importService")
// Let Spring manage our transactions
@Transactional
public class ImportServiceImpl implements ImportService
{
	private static final Logger logger = Logger.getLogger(ImportService.class);
	private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;

	@Autowired
	@Qualifier("badgeConfigDao")
	private BadgeConfigDao badgeConfigDao;

	// @Autowired
	// @Qualifier("dtgConfigDao")
	// private BadgeConfigDao dutyToGodConfigDao;

	@Autowired
	@Qualifier("organizationDao")
	private OrganizationDao organizationDao;
	@Autowired
	private BaseUnitTypeDao baseUnitTypeDao;
	@Autowired
	@Qualifier("rankConfigDao")
	private RankConfigDao rankConfigDao;

	@Autowired
	@Qualifier("awardConfigDao")
	private AwardConfigDao awardConfigDao;

	@Autowired
	@Qualifier("requirementConfigDao")
	private RequirementConfigDao requirementConfigDao;
	
	@Autowired
	private TraxService traxService;


	public ImportServiceImpl()
	{
	}

	public void updateUserImage(CommonsMultipartFile fileData, Scout scout)
	{
		if (!fileData.getContentType().contains("image"))
		{
			String error = "Error: Invalid filetype of " + fileData.getContentType();
			System.err.println(error);
		}
		byte[] imageInByte = fileData.getBytes();
		// byte[] imageInByte = resizeImage(fileData.getBytes());
		scout.setProfileImage(imageInByte);
		userDao.save(scout);
	}

	private byte[] resizeImage(byte[] imageInByte)
	{
		int width = 100;
		int height = 140;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			InputStream in = new ByteArrayInputStream(imageInByte);
			BufferedImage bImageFromConvert = ImageIO.read(in);
			if (bImageFromConvert.getHeight() == height && bImageFromConvert.getWidth() == width)
			{
				return imageInByte; // do nothing its already the correct size
			}

			BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(bImageFromConvert, 0, 0, width, height, null);
			g.dispose();
			ImageIO.write(resizedImage, "jpg", baos);

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public Organization updateScoutNetData(Organization organization, CommonsMultipartFile fileData, User leader)
			throws Exception
	{
		if (!(fileData.getContentType().equals("application/pdf") 
				|| fileData.getContentType().equals("application/octet-stream")
				|| fileData.getContentType().equals("application/octetstream")))
		{
			String error = "Error: Invalid filetype of " + fileData.getContentType();
			System.err.println(error);
			throw new Exception(error);
		}

		/**
		 * org can never be null... if (organization==null) { organization = new
		 * Organization(); organization.setUnits(new HashSet<Unit>()); }
		 */
		String text = pdfToText(fileData);
		Unit unit = updateOrganization(organization, text);

		try
		{
			Map<String, Collection<AwardCompletion>> scoutAwardMap = getScoutAwardMap(text);

			// successfully imported, now create in the db
			for (Iterator iterator = scoutAwardMap.keySet().iterator(); iterator.hasNext();)
			{
				String scoutFullname = (String) iterator.next();
				// break the name down, trim out any leading spaces -- it can happen
				String[] scoutFullnameSplit = scoutFullname.trim().split(" ");
				int i = 0;

				int count = scoutFullnameSplit.length;
				String firstName = scoutFullnameSplit[i];
				String middleName = null;
				String nameSuffix = null;
				if (count == 3 || count == 4)
				{
					middleName = scoutFullnameSplit[++i];
				}
				String lastName = scoutFullnameSplit[++i];
				if (count == 4)
				{
					nameSuffix = scoutFullnameSplit[++i];
				}

				Scout scout = (Scout) userDao.getUserByFullnameAndOrg(firstName, middleName, lastName,
						organization.getId());

				if (scout == null)
				{
					// not found create a new one
					organization = organizationDao.findById(organization.getId(), false);
					scout = new Scout(organization, unit, firstName, middleName, lastName, nameSuffix, leader.getZip());
					scout.setUnitCopy(unit);//each scout must have his own
					traxService.addRanks(scout);
				}

				if (scout.getRetired())
				{
					/** if the user is retired, and they are importing, go ahead and re activate	 */
					scout.setRetired(false);
				}

				Collection<AwardCompletion> ac = scoutAwardMap.get(scoutFullname);
				Map scoutNotFoundAwards = updateScoutAwards(leader, scout, ac);
				// TODO if scoutNotFoundAwards != null then let the user know
				// what was not imported

				if (scout.getId() == 0)
				{
					scout.setUnit(unit);
					userDao.save(scout);
				}
				else
				{
					userDao.persist(scout);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return organization;
	}

	/**
	 * this may be reused depending for any type of import
	 * 
	 * @param leader
	 * @param scout
	 * @param awardDateMap
	 */
	private Map<String, String> updateScoutAwards(User leader, Scout scout, Collection<AwardCompletion> ac)
	{
		Map<String, String> awardsNotFound = new HashMap<String, String>();

		for (Iterator iterator2 = ac.iterator(); iterator2.hasNext();)
		{
			boolean hasAward = false;
			AwardCompletion awardCompletion = (AwardCompletion) iterator2.next();

			for (Award dbAward : scout.getAwards())
			{
				if (dbAward.getAwardConfig().getName().equalsIgnoreCase(awardCompletion.getAwardName()))
				{
					hasAward = true;

					if (dbAward.getDateCompleted() != null)
					{
						continue; //just believe the one in scouttrax
					}

					dbAward.setDateCompleted(awardCompletion.getCompletionDate());
					if (dbAward.getUser() == null)
					{
						dbAward.setUser(leader);
					}
					/*
					 * don't update requirements, just the award this works, but
					 * might not be necessary
					 */
					Set<Requirement> earnedRequirements = dbAward.getRequirements();
					Set<Requirement> requirements = new HashSet<Requirement>();
					for (RequirementConfig rc : dbAward.getAwardConfig().getRequirementConfigs())
					{
						Requirement r = null;
						if (earnedRequirements != null)
						{
							for (Requirement earnedRequirement : earnedRequirements)
							{
								if (earnedRequirement.getRequirementConfig().getId() == rc.getId())
								{
									// same one don't update
									r = earnedRequirement;
									break;
								}
							}
						}
						if (r == null)
						{
							r = new Requirement();
							r.setDateCompleted(awardCompletion.getCompletionDate());
							r.setRequirementConfig(rc);
							r.setUser(leader);
						}

						requirements.add(r);
					}
					dbAward.setRequirements(requirements);

					// break; //break out of the inner for loop - it has been
					// found move on to next one
				}
			}
			if (!hasAward)
			{
				// could be badge or award, check both (most likely a badge, so
				// start there
				Award award = null;
				AwardConfig awardConfig = badgeConfigDao.getByName(awardCompletion.getAwardName());
				if (awardConfig == null)
				{
					awardConfig = awardConfigDao.getByName(awardCompletion.getAwardName());
					if (awardConfig == null)
					{
						awardsNotFound.put(scout.getFullName(), awardCompletion.getAwardName());
						logger.debug("%%% failed to find Award " + awardCompletion.getAwardName() + " for "
								+ scout.getFullName());
						continue; // skip it
					}
				}

				if (awardConfig instanceof BadgeConfig)
				{
					award = new Badge();
				}
				else if (awardConfig instanceof RankConfig)
				{
					award = new Rank();
				}
				else
				{
					award = new Award();
				}

				award.setAwardConfig(awardConfig);
				award.setDateCompleted(awardCompletion.getCompletionDate());
				award.setUser(leader);

				Set<Requirement> requirements = new HashSet<Requirement>();
				
				for (RequirementConfig rc : awardConfig.getRequirementConfigs())
				{
					Requirement r = new Requirement();
					r.setDateCompleted(awardCompletion.getCompletionDate());
					r.setRequirementConfig(rc);
					r.setUser(leader);
					requirements.add(r);
				}
				award.setRequirements(requirements);

				scout.getAwards().add(award);
				logger.info("***" + awardConfig.getName());
			}
		}

		return awardsNotFound;
	}

	private Map<String, Collection<AwardCompletion>> getScoutAwardMap(String text) throws IOException, ParseException
	{
		String TIGER = "Tiger Cubs";
		String CUB = "Cub Scouts";
		String WEBELOS = "Webelos Scouts";
		String awardKey = "Award";
		String badgeKey = "Badge";
		String rankKey = "Rank";
		String beltLoopKey = "Belt Loop";
		String dtgKey = "DTG";
		BufferedReader reader = new BufferedReader(new StringReader(text));

		String line;
		Map<String, Collection<AwardCompletion>> scoutAwardMap = new HashMap<String, Collection<AwardCompletion>>();
		String completionDateString = "";
		String awardName = "";
		String fullname = "";
		boolean skippingHeader = true;
		boolean parsingNotAdvancingNames = false;
		
		while ((line = reader.readLine()) != null)
		{
			if (line.length() > 0)
			{

				if (skippingHeader
						&& !(line.equalsIgnoreCase(awardKey) //skip until one of these is found
								|| line.equalsIgnoreCase(badgeKey)
								|| line.equalsIgnoreCase(rankKey) 
								|| line.equalsIgnoreCase(dtgKey) 
								|| line.startsWith("Member Name") 
								
								/*|| line.equalsIgnoreCase(TIGER) 
								|| line.equalsIgnoreCase(CUB) 
								|| line.equalsIgnoreCase(WEBELOS)*/))
				{
					continue;
				}
				else
				{
					skippingHeader = false;
				}

				if ( line.startsWith("Total") )
				{
					break; // done parsing 
				}
				/*if(line.equalsIgnoreCase(TIGER))
				{
					//create a new unit, and set the boys unit type to either Tiger, Wolf or Webelos
					continue;
				}
				else if(line.equalsIgnoreCase(CUB))
				{
					continue;
				}
				else if(line.equalsIgnoreCase(WEBELOS))
				{
				
				}*/
					
				if(line.startsWith("Following is")
					|| line.startsWith("should be"))
				{
					parsingNotAdvancingNames = true;
					continue;
				}
				if (!(line.equalsIgnoreCase(awardKey) //skip lines with any of these words
					|| line.equalsIgnoreCase(badgeKey)
					|| line.equalsIgnoreCase(rankKey) 
					|| line.equalsIgnoreCase(beltLoopKey)
					|| line.contains("file:") 
					|| line.contains("Internet")
					|| line.contains("Member Name") //cub only
					|| line.equalsIgnoreCase(TIGER) 
					|| line.equalsIgnoreCase(CUB) 
					|| line.equalsIgnoreCase(WEBELOS)))
				{
					
					String[] lineWords = line.split(" ");
					boolean beforeDate = true;

					for (String element : lineWords)
					{
						if(isDate(element))
						{
							completionDateString = element;
					    	beforeDate = false;
						}
						else if (beforeDate)
						{
							fullname += element + " ";
						}
						else
						{
							awardName += element + " ";
						}
					}
					
					if(parsingNotAdvancingNames)
					{
						//even if boys have no awards, add them to the list
						if (!scoutAwardMap.containsKey(fullname))
						{
							scoutAwardMap.put(fullname, new ArrayList<AwardCompletion>());
							System.out.println(fullname);
						}
					}
					else
					{
						// scout data maybe on one or two lines, if it is on two we
						// need to read the next
						// line before we will have to read the 2nd line to get the
						// rest of the data
						if (completionDateString.equals("") || scoutSpansPageBreak(scoutAwardMap, fullname))
						{
							continue;
						}
						
						// we now have the complete award record for one award
						// collect all the awards for one boy,
						awardName = awardName.replace("*", "").trim();
						System.out.println(fullname + " " + completionDateString + " " + awardName);
						AwardCompletion ac = new AwardCompletion(awardName, completionDateString);
					
						if (!scoutAwardMap.containsKey(fullname))
						{
							scoutAwardMap.put(fullname, new ArrayList<AwardCompletion>());
						}
						scoutAwardMap.get(fullname).add(ac);
					}
					fullname = "";
					awardName = "";
					completionDateString = "";
				}
			}
		}
		reader.close();
		return scoutAwardMap;
	}

	private boolean scoutSpansPageBreak(Map<String, Collection<AwardCompletion>> scoutAwardMap, String fullname)
	{
		for (String scoutFullname : scoutAwardMap.keySet())
		{
			if(! scoutFullname.equals(fullname))
			{
				//either we are switching scouts, or a scout record splits a page
				//Charles Frederick
				//McGuire 10/16/2013 Rifle Shooting
				//Charles Frederick 07/19/2014 Space Exploration
				//Internet Advancement - Unit Advancement Summary Page 9 of 13
				//file://E:\Websites\InternalServices\System\ConversionServer\SystemService\Input\A9A02... 1/24/2015
				//McGuire
				if (scoutFullname.startsWith(fullname))
				{
					return true;
				}
			}
		}
		return false;
	}

	private boolean isDate(String element) 
	{
	    try
	    {
	    	// (2) give the formatter a String that matches the SimpleDateFormat pattern
	    	formatter.parse(element);
	    	return true;
	    }
	    catch (ParseException e)
	    {
	      // this is not a date, it may be part of an award name
	    	return false;
	    }
	}

	/**
	 * update an existing unit to match what the council has
	 * 
	 * @param organization
	 * @param text
	 * @return
	 */
	private Unit updateOrganization(Organization organization, String text)
	{
		Unit unit = null;

		String unitKey = "Unit: ";
		String coKey = "Chartered Organization: ";
		String districtKey = "District: ";
		String councilKey = "Council: ";
		String expire = "Unit Expire Date: ";

		// refresh the organization
		organization = organizationDao.findById(organization.getId(), false);
		String[] nameNumber = getToken(text, unitKey, coKey).split(" ");
		String unitTypeName = nameNumber[0];
		Integer unitNumber = Integer.valueOf(nameNumber[1]);
		for (Unit myUnit : organization.getUnits())
		{
			if (myUnit.getNumber().equals(unitNumber) //&& ("Pack".equals(unitTypeName) 
					&& myUnit.getTypeOfUnit().getName().equalsIgnoreCase(unitTypeName))//)
			{
				// must check number and typename since eleven year old troops
				// and 12-13 year old troops share the same number
				unit = myUnit;
				break;
			}
		}

		if (unit == null)
		{
			// add a new one
			unit = new Unit(baseUnitTypeDao.find(unitTypeName), unitNumber, organization);
			organization.getUnits().add(unit);
		}
		organization.setName(getToken(text, coKey, districtKey));
		organization.setDistrict(getToken(text, districtKey, councilKey));
		organization.setCouncil(getToken(text, councilKey, expire));
		organizationDao.persist(organization);

		return unit;
	}

	private String getToken(String text, String first, String last)
	{
		int start = text.indexOf(first) + first.length();
		int end = text.indexOf(last);
		return text.substring(start, end).replaceAll("\r\n", " ");
	}
	
	private String pdfToText(CommonsMultipartFile fileData)
	{
		String parsedText = "";
		COSDocument cosDoc = null;
		PDDocument pdDoc = null;

		try
		{
			PDFParser parser = new PDFParser(fileData.getFileItem().getInputStream());
			parser.parse();
			cosDoc = parser.getDocument();
			
			PDFTextStripper pdfStripper = new PDFTextStripper();
			pdDoc = new PDDocument(cosDoc);
			
			parsedText = pdfStripper.getText(pdDoc);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				cosDoc.close();
				pdDoc.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return parsedText;
	}

	class AwardCompletion
	{
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		private String awardName;
		private Date completionDate;

		public AwardCompletion(String awardName, String completionDate) throws ParseException
		{
			this.awardName = awardName.trim();
			this.completionDate = df.parse(completionDate);
		}

		public String getAwardName()
		{
			return awardName;
		}

		public Date getCompletionDate()
		{
			return completionDate;
		}
	}

	public List<AwardConfig> parseAwards(CommonsMultipartFile fileData) throws Exception
	{
		if (!fileData.getContentType().equals("text/plain"))
		{
			String error = "Error: Invalid filetype of " + fileData.getContentType();
			System.err.println(error);
			throw new Exception(error);
		}
		BufferedReader bis = null;
		List<AwardConfig> acList = new LinkedList<AwardConfig>();
		try
		{
			InputStream is = fileData.getFileItem().getInputStream();
			bis = new BufferedReader(new InputStreamReader(is));
			// Keep reading from the file while there is any content
			// when the end of the stream has been reached, -1 is returned
			String line = null;
			int i = 0;
			int idIndex = 0;
			int parentAwardConfigIdIndex=0;
			int nameIndex = 0;
			int kindIndex = 0;
			int descriptionIndex = 0;
			int requiredIndex = 0;
			int sortOrderIndex = 0;
			int linkIndex = 0;

			while ((line = bis.readLine()) != null)
			{
				if(line.trim().length()==0)
				{
					//ignore blank lines
					continue;
				}
				String[] split = line.split("\t");
				if (i++ == 0)
				{
					// its the header

					for (int j = 0; j < split.length; j++)
					{
						String columnName = split[j].replaceAll("\"", "").trim(); 
						if (columnName.equalsIgnoreCase("Kind"))
						{
							kindIndex = j;
						}
						else if (columnName.equalsIgnoreCase("Id"))
						{
							idIndex = j;
						}
						else if (columnName.equalsIgnoreCase("Name"))
						{
							nameIndex = j;
						}
						else if (columnName.equalsIgnoreCase("Description"))
						{
							descriptionIndex = j;
						}
						else if (columnName.equalsIgnoreCase("Required"))
						{
							requiredIndex = j;
						}
						else if (columnName.equalsIgnoreCase("SortOrder"))
						{
							sortOrderIndex = j;
						}
						else if (columnName.equalsIgnoreCase("Link"))
						{
							linkIndex = j;
						}
						else if (columnName.equalsIgnoreCase("parentAwardConfigId"))
						{
							parentAwardConfigIdIndex = j;
						}
						else
						{
							System.out.println("****" + split[j] + " is not a valid column name");
							// throw new
							// Exception(split[j]+" is not a valid column name");
						}
					}
				}
				else
				{
					try
					{
						String kind = split[kindIndex].replaceAll("\"", "");
						String name = split[nameIndex].replaceAll("\"", "").trim();
						String description = split[descriptionIndex].replaceAll("\"", "").trim();
						Boolean required = split[requiredIndex].replaceAll("\"", "").equals("1");
						int sortOrder = Integer.valueOf(split[sortOrderIndex].replaceAll("\"", ""));
						//ignore for now, not sure how to do a selfjoin - - long parentAwardConfigId = Long.valueOf(split[parentAwardConfigIdIndex]);
						String link=null;
						try 
						{
							link = split[linkIndex].replaceAll("\"", "");
						}
						catch(ArrayIndexOutOfBoundsException aioobe)
						{
							//ignore this, it is not a required field
						}
						AwardConfig ac = null;
						switch(kind)
						{
							case "A": ac = new AwardConfig(); break;
							case "B": ac = new BadgeConfig(); break;
							case "R": ac = new RankConfig();  break;
							case "D": ac = new DutyToGodConfig(); break;
						//Cub
							case "C": ac = new CubRankConfig(); break;
							case "E": ac = new CubRankElectiveConfig(); break;
							case "G": ac = new ActivityBadgeConfig(); break;
							case "L": ac = new BeltLoopConfig(); break; 
							case "P": ac = new PinConfig();	break;
							case "T": ac = new CourseConfig(); break;
							case "I": ac = new Cub2015RankConfig(); break;
							case "J": ac = new ChildAwardConfig(); break;
							case "K": ac = new Cub2015RankElectiveConfig(); break;
						default:
							throw new Exception(kind
									+ " is not a valid award Type, 'A', 'B', 'D', 'T' or 'R' for Scout and 'C', 'E', 'G', 'L', 'P', 'I', 'J', 'K' for cub are acceptable");
						} 
						ac.setName(name);
						ac.setDescription(description);
						ac.setRequired(required);
						ac.setSortOrder(sortOrder);
						ac.setLink(link);
						acList.add(ac);
					}
					catch (Exception e)
					{
						// ignore and keep going?
						e.printStackTrace();
					}
				}
			}
		}
		finally
		{
			try
			{
				if (bis != null)
					bis.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return acList;
	}

	public void saveAwardData(Collection<AwardConfig> awardConfigs) throws Exception
	{
		for (AwardConfig ac : awardConfigs)
		{
			AwardConfig dbac = awardConfigDao.getByName(ac.getName());
			try
			{
				if (dbac == null)
				{
					awardConfigDao.save(ac);
				}
				else
				{
					dbac.setDescription(ac.getDescription());
					dbac.setName(ac.getName());
					dbac.setRequired(ac.getRequired());
					dbac.setSortOrder(ac.getSortOrder());
					awardConfigDao.persist(dbac);
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Set<RequirementConfig> parseRequirements(CommonsMultipartFile fileData) throws Exception
	{
		if (!fileData.getContentType().equals("text/plain") && !fileData.getContentType().equals("application/vnd.ms-excel"))
		{
			String error = "Error: Invalid filetype of " + fileData.getContentType();
			System.err.println(error);
			throw new Exception(error);
		}
		Set<RequirementConfig> rcList = new HashSet<RequirementConfig>();
		CSVReader br = null;
		String[] split = null;
		try
		{
			InputStream is = fileData.getFileItem().getInputStream();
			//br = new BufferedReader(new InputStreamReader(is));
			br = new CSVReader(new InputStreamReader(is));
			// Keep reading from the file while there is any content
			// when the end of the stream has been reached, -1 is returned
			
			int i = 0;
			int textIndex = 0;
			int leaderAuthorizedIndex = 0;
			int awardConfigIdIndex = 0;
			int sortOrderIndex = 0;
			RequirementConfig rc = null;
			Long awardConfigId = 0L;
			String text = "";
			Boolean isLeaderAuthorized = false;
			int sortOrder = 0;

			while ((split = br.readNext()) != null)
			{
				//String[] split = line.split(",");
				boolean updateNeeded = true;

				if (i++ == 0)
				{
					// its the header
					for (int j = 0; j < split.length; j++)
					{
//						if (split.length == 1)
//						{
//							split = line.split(",");
//						}
						String columnName = split[j].replaceAll("\"", "").replace("\u00ef", "").replace("\u00bf", "")
								.replace("\u00bb", "");
						
						if (columnName.equalsIgnoreCase("awardConfigId"))
						{
							awardConfigIdIndex = j;
						}
						else if (columnName.equalsIgnoreCase("isLeaderAuthorized"))
						{
							leaderAuthorizedIndex = j;
						}
						else if (columnName.equalsIgnoreCase("Text"))
						{
							textIndex = j;
						}
						else if (columnName.equalsIgnoreCase("SortOrder"))
						{
							sortOrderIndex = j;
						}
						else
						{
							//throw new Exception(split[j] + " is not a valid column name");
						}
					}
				}
				else //its data
				{
					String sortOrderString = split[sortOrderIndex];
					if(sortOrderString==null || sortOrderString.trim().length()==0)
					{
						//must be empty, continue 
						continue;
					}
					sortOrder = Integer.valueOf(sortOrderString);
					awardConfigId = Long.valueOf(split[awardConfigIdIndex]);
					text = split[textIndex];
					int leaderAuthorized = Integer.valueOf(split[leaderAuthorizedIndex].replaceAll("\"", ""));
					isLeaderAuthorized = leaderAuthorized == 1;
					addRequirementConfig(rcList, awardConfigId, text, isLeaderAuthorized, sortOrder,
							updateNeeded);
					System.out.println(text);
					/*boolean isRequirementText = true;
					try
					{
						findInt = Integer.valueOf(split[sortOrderIndex].replaceAll("\"", ""));
						isRequirementText = false;
					}
					catch (Exception e)
					{
						isRequirementText = true;
					}

					if (isRequirementText)
					{
						// continuation of previous line
						// the whole line needs to be added to the previous text
						for (int j = 0; j < split.length; j++)
						{
							text += "\n" + split[j];
						}
						text = text.replaceAll("\"", "");// .replaceAll("[^\\p{ASCII}]",
															// "");//get rid of
															// all double quotes
					}
					else
					
					{
						if (awardConfigId != 0)// skip it the first time
						{
							// write out the last one, if updatedNeeded = true
							// the requirement text is complete
							addRequirementConfig(rcList, awardConfigId, text, isLeaderAuthorized, sortOrder,
									updateNeeded);
							System.out.println(text);
						}

						// this is the the first line
						sortOrder = findInt;
						awardConfigId = Long.valueOf(split[awardConfigIdIndex].replaceAll("\"", ""));
						int leaderAuthorized = Integer.valueOf(split[leaderAuthorizedIndex].replaceAll("\"", ""));
						isLeaderAuthorized = leaderAuthorized == 1;

						text = split[textIndex].replaceAll("\"", "");
						if (textIndex < split.length)//only works if text is last
						{
							// there is a comma in the text, keep adding it,
							// until we have the full line
							for (int j = textIndex + 1; j < split.length; j++)
							{
								Pattern integerPattern = Pattern.compile("^\\d*$");
								Matcher matchesInteger = integerPattern.matcher(split[j].replaceAll("\"", ""));
								boolean isInteger = matchesInteger.matches();
								if(!isInteger)
								{
									text += split[j];
								}
							}
						}
					}
					*/
				}
			}
		}
		finally
		{
			try
			{
				if (br != null)
					br.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		return rcList;
	}

	private void addRequirementConfig(Set<RequirementConfig> rcList, Long awardConfigId, String text,
			Boolean isLeaderAuthorized, int sortOrder, boolean updateNeeded) throws Exception
	{
		RequirementConfig rc;
		AwardConfig ac = awardConfigDao.findById(awardConfigId, false);
		if (ac != null)
		{
			List<RequirementConfig> oldRcList = ac.getRequirementConfigs();
			for (RequirementConfig oldRc : oldRcList)
			{
				if (oldRc.getSortOrder() == sortOrder)
				{
					if (isLeaderAuthorized == oldRc.getLeaderAuthorized() && oldRc.getText().startsWith(text))// kludge
																												// //.replaceAll("[^\\p{ASCII}]",
																												// "")
					{
						updateNeeded = false; // no difference
						break;
					}
					rc = oldRc; // updating old requirement
					break;
				}
			}

			if (updateNeeded)
			{
				// always create a new, since we don't want these tied to any db
				// objects at this time
				// we are reviewing them for now...
				rc = new RequirementConfig();
				rc.setAwardConfig(ac);
				rc.setLeaderAuthorized(isLeaderAuthorized);
				rc.setText(text);
				rc.setSortOrder(sortOrder);
				rcList.add(rc);
			}
		}
		else
		{
			throw new Exception("Cannot find the award with awardConfigId " + awardConfigId
					+ " You must add the award before requirements");
		}
	}

	public void saveRequirementConfigs(Collection<RequirementConfig> requirementConfigs) throws Exception
	{
		for (RequirementConfig rc : requirementConfigs)
		{
			boolean found = false;
			try
			{
				// if it already exists throw an exception
				AwardConfig ac = awardConfigDao.findById(rc.getAwardConfig().getId(), false);
				Collection<RequirementConfig> existingRcs = ac.getRequirementConfigs();
				if (existingRcs == null)
				{
					existingRcs = new ArrayList<RequirementConfig>();
				}
				for (RequirementConfig existingRc : existingRcs)
				{
					// already know this is for the same awardconfigid
					if (existingRc.getSortOrder() == rc.getSortOrder())
					{
						found = true;
						// found it, I hope
						if (rc.getLeaderAuthorized() == existingRc.getLeaderAuthorized()
								&& rc.getText().equals(existingRc.getText()))
						{
							break; // found but no difference
						}
						else
						// seems to be a problem here
						{
							logger.error("\n\nTrying to change requirement for award '" + rc.getAwardConfig().getName()
									+ "'\n\t The requirement that already exists, with text '" + existingRc.getText()
									+ "'\n\t will be replaced with '" + rc.getText() + "'");
							// don't allow updating of existing ones, it causes
							// a concurrent update exception
							existingRc.setLeaderAuthorized(rc.getLeaderAuthorized());
							existingRc.setText(rc.getText());
						}
						break;
					}
				}
				if (!found)
				{
					existingRcs.add(rc);
				}
				awardConfigDao.persist(ac);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
