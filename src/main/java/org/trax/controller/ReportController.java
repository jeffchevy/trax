package org.trax.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.lang.time.DateUtils;
import org.jfree.util.Log;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.trax.dto.NameComplete;
import org.trax.dto.ScoutReportDto;
import org.trax.form.ReportForm;
import org.trax.model.Award;
import org.trax.model.AwardConfig;
import org.trax.model.BadgeConfig;
import org.trax.model.DutyToGodConfig;
import org.trax.model.Organization;
import org.trax.model.RankConfig;
import org.trax.model.Requirement;
import org.trax.model.RequirementConfig;
import org.trax.model.Scout;
import org.trax.model.User;
import org.trax.model.cub.ActivityBadgeConfig;
import org.trax.model.cub.BeltLoopConfig;
import org.trax.model.cub.CubAwardConfig;
import org.trax.model.cub.CubRankConfig;
import org.trax.model.cub.CubRankElectiveConfig;
import org.trax.model.cub.PinConfig;

@Controller
public class ReportController extends AbstractScoutController
{
	@RequestMapping("/scoutreport.html")
	public ModelAndView createScoutReport(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		ModelAndView mav = new ModelAndView("scoutreport");
		List<Scout> scouts = getScouts(request);
		List<ScoutReportDto> scoutReports = new ArrayList<ScoutReportDto>();
		for (Scout scout : scouts)
		{
			if (scout.isSelected() || scout.isChecked())
			{
				scout = traxService.refreshScout(scout); // refresh
				ScoutReportDto scoutDto = new ScoutReportDto();
				scoutDto.setCub(scout.getUnit().isCub());
				scoutDto.setFullname(scout.getFullName());
				scoutDto.setBsaMemberId(scout.getBsaMemberId());
				scoutDto.setPositionName(((scout.getPosition() != null && scout.getPosition().getPositionName() != null) ? scout.getPosition().getPositionName() : ""));
				scoutDto.setAwards(scout.getAwards());
				scoutDto.setCampEntries(scout.getCampEntries());
				scoutDto.setServiceEntries(scout.getServiceEntries());
				scoutDto.setLeadershipEntries(scout.getLeadershipEntries());
				scoutDto.setBirthday(scout.getBirthDate());
				scoutReports.add(scoutDto);
			}
		}
		mav.addObject("scoutReports", scoutReports);
		List<BadgeConfig> requiredMeritBadges = traxService.getRequiredMeritBadges();
		mav.addObject("requiredMeritBadges", requiredMeritBadges);

		return mav;
	}

	@RequestMapping(value = "/report.html", method = RequestMethod.GET)
	public ModelAndView report(HttpServletRequest request, Map<String, Object> model, @RequestParam(value = "message", required = false) String message)
	{
		request.getSession().setAttribute("navigationItem", "redirect:report.html");

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Organization organization = user.getOrganization();
		Collection<User> troop = traxService.getUsers(organization.getId(), user.getUnit().isCub());
		model.put("users", troop);
		// model.put("selection", new SelectionForm());
		ReportForm reportForm = new ReportForm();
		Calendar startCalendar = Calendar.getInstance();
		// default to 3 months earlier since Court of Honors are suppose to be
		// held every three months
		startCalendar.add(Calendar.MONTH, -3);
		reportForm.setStartDate(startCalendar.getTime());
		reportForm.setEndDate(DateUtils.addDays(new Date(), 1));// add a day to
																// make it
																// inclusive);
		model.put("reportForm", reportForm);

		model.put("successMessage", message);
		return new ModelAndView("report", model);
	}

	@RequestMapping(value = "/createreport.html", method = RequestMethod.GET)
	public String createGroupReport(Map<String, Object> model, HttpServletRequest request, @Valid ReportForm reportForm, BindingResult result) throws IOException
	{
		if (result.hasErrors())
		{
			// for (ObjectError error : result.getAllErrors())
			// {
			model.put("errorMessage", "Both dates are required");
			// }
			model.put("reportForm", reportForm);

			return "report";
		}
		boolean isCub = false;
		Map<String, List<NameComplete>> awardScoutMap = new HashMap<String, List<NameComplete>>();
		Map<String, List<NameComplete>> requiredScoutMap = new HashMap<String, List<NameComplete>>();
		Map<String, List<NameComplete>> rankScoutMap = new HashMap<String, List<NameComplete>>();
		Map<String, List<NameComplete>> pinMap = new HashMap<String, List<NameComplete>>();
		Map<String, List<NameComplete>> beltLoopMap = new HashMap<String, List<NameComplete>>();
		Map<String, List<NameComplete>> electiveMap = new HashMap<String, List<NameComplete>>();
		Map<String, List<NameComplete>> activityBadgeMap = new HashMap<String, List<NameComplete>>();
		Map<String, List<NameComplete>> dtgMap = new HashMap<String, List<NameComplete>>();

		@SuppressWarnings("unchecked")
		List<Scout> scouts = (List<Scout>) request.getSession().getAttribute("scouts");
		for (Scout scout : scouts)
		{
			scout = traxService.refreshScout(scout);
			if (scout.isSelected() || scout.isChecked())
			{
				isCub = scout.getUnit().isCub();
				for (Award award : scout.getAwards())
				{
					String indexAndName = award.getAwardConfig().getSortOrder() + ":" + award.getAwardConfig().getName();
					String scoutName = scout.getFullName();
					boolean hasBeadsAndArrows = isCub && (award.getAwardConfig() instanceof CubRankConfig || award.getAwardConfig() instanceof CubRankElectiveConfig);
					if (award.getDateCompleted() != null || hasBeadsAndArrows)
					{
						// need someway of adding information something so we
						// can display sort boys by oldest to youngest also so
						// column and row totals
						// if (award.getDateCompleted() != null)
						{
							// filter by start and end date
							Date startDate = reportForm.getStartDate();
							Date endDate = reportForm.getEndDate();
							// double percent = Math.ceil((((double)
							// award.getRequirements().size())/
							// award.getAwardConfig().getRequirementConfigs().size()
							// * 100));
							// String complete = (String)
							// (award.getDateCompleted() == null ? percent + "%"
							// : formatter.format(award.getDateCompleted()));
							boolean withinDateRange = onOrBetween(award.getDateCompleted(), startDate, endDate);
							if (withinDateRange || hasBeadsAndArrows)
							{
								boolean canShowDate = award.getDateCompleted() != null && withinDateRange;
								String formattedDate = canShowDate ? formatter.format(award.getDateCompleted()) : "";
								boolean awarded = award.getDateAwarded()!=null;
								boolean purchased = award.getDatePurchased()!=null;
								NameComplete nameComplete = new NameComplete(scoutName, formattedDate, awarded, purchased);
								if (scout.getUnit().isCub())
								{
									if (award.getAwardConfig() instanceof BeltLoopConfig)
									{
										addNameComplete(beltLoopMap, indexAndName, nameComplete);
									}
									else if (award.getAwardConfig() instanceof PinConfig)
									{
										addNameComplete(pinMap, indexAndName, nameComplete);
									}
									else if (award.getAwardConfig() instanceof CubRankElectiveConfig)
									{
										String beadsText = generateArrowPointTexts(award, startDate, endDate);
										nameComplete = new NameComplete(scoutName, nameComplete.getComplete() + beadsText, awarded, purchased);
										addNameComplete(electiveMap, indexAndName, nameComplete);
									}
									else if (award.getAwardConfig() instanceof ActivityBadgeConfig)
									{
										addNameComplete(activityBadgeMap, indexAndName, nameComplete);
									}
									else if (award.getAwardConfig() instanceof CubRankConfig)
									{
										String beadsText = generateBeadsText(award, startDate, endDate);
										nameComplete = new NameComplete(scoutName, nameComplete.getComplete() + beadsText, awarded, purchased);
										// rankCount++;
										// if they earn the award, they
										// automatically get the beads
										// if(isWolf){wolfBeadCount=4;}else{bearBeadCount=4;}
										addNameComplete(rankScoutMap, indexAndName, nameComplete);
									}
									else if (award.getAwardConfig() instanceof AwardConfig)// cub and scout
									{
										// awardCount++;
										addNameComplete(awardScoutMap, indexAndName, nameComplete);
									}
									else
									{
										// must be a scout award, we don't want
										// to show it --should never happen
										System.out.println("***Not showing " + indexAndName + " is not valid for a cub scout");
										continue;
									}
								}
								else
								{
									// boyscouts still carry their cub awards
									// -hide it here
									if (award.getAwardConfig() instanceof BeltLoopConfig || award.getAwardConfig() instanceof PinConfig
													|| award.getAwardConfig() instanceof CubRankElectiveConfig || award.getAwardConfig() instanceof ActivityBadgeConfig
													|| award.getAwardConfig() instanceof CubRankConfig || award.getAwardConfig() instanceof DutyToGodConfig
													|| award.getAwardConfig() instanceof CubAwardConfig)
									{
										continue;
									}

									if (award.getAwardConfig() instanceof RankConfig)
									{
										addNameComplete(rankScoutMap, indexAndName, nameComplete);
									}
									else if (award.getAwardConfig().isRequired())
									{
										addNameComplete(requiredScoutMap, indexAndName, nameComplete);
									}
									else
									{
										addNameComplete(awardScoutMap, indexAndName, nameComplete);
									}
								}
							}
						}
					}
				}
			}
		}
		// cub only
		request.setAttribute("pinMap", pinMap);
		request.setAttribute("beltLoopMap", beltLoopMap);
		request.setAttribute("electiveMap", electiveMap);
		request.setAttribute("activityBadgeMap", activityBadgeMap);
		request.setAttribute("dtgMap", dtgMap);
		// all scouts
		request.setAttribute("awardScoutMap", awardScoutMap);
		request.setAttribute("rankScoutMap", rankScoutMap);
		request.setAttribute("requiredScoutMap", requiredScoutMap);
		request.getSession().setAttribute("todaysDate", new Date());

		return isCub ? "groupCubReport" : "groupReport";
	}

	private void addNameComplete(Map<String, List<NameComplete>> awardScoutMap, String indexAndName, NameComplete nameComplete)
	{
		if (awardScoutMap.containsKey(indexAndName))
		{
			awardScoutMap.get(indexAndName).add(nameComplete); // use index to
																// preserve
																// order
		}
		else
		{
			List<NameComplete> nameCompleteList = new ArrayList<NameComplete>();
			nameCompleteList.add(nameComplete);
			awardScoutMap.put(indexAndName, nameCompleteList);
		}
	}

	private String generateBeadsText(Award award, Date startDate, Date endDate)
	{
		String beadsText = "";
		boolean isWolf = award.getAwardConfig().getName().equals("Wolf");
		boolean isBear = award.getAwardConfig().getName().equals("Bear");
		if (isWolf || isBear)
		{
			// count all requirements with a number, for each 3 they earn a bead
			int numberedRequirements = 0;
			int completedBefore = 0;
			for (Requirement requirement : award.getRequirements())
			{
				String text = requirement.getRequirementConfig().getText();
				if (text !=null && !text.isEmpty()
					&& Character.isDigit(text.charAt(0)))
				{
					if (onOrBefore(requirement.getDateCompleted(), startDate))
					{
						completedBefore++;
					}
					if (onOrBetween(requirement.getDateCompleted(), startDate, endDate))
					{
						numberedRequirements++;
					}
				}
			}
			// don't count more than 12 for bear
			numberedRequirements = numberedRequirements > 12 ? 12 : numberedRequirements; 
			int numberOfBeads = numberedRequirements / 3;
			if (numberOfBeads != 0)
			{
				beadsText = "(" + numberOfBeads + (isWolf ? " Yellow" : " Red") + " bead" + (numberOfBeads > 1 ? "s" : "") + ")";
			}
			if (award.getRequirements().size() == 0 && (award.getDateCompleted() != null && onOrBetween(award.getDateCompleted(), startDate, endDate)))
			{
				beadsText = " (4 " + (isWolf ? "Yellow" : "Red") + " beads)";
			}
		}
		return beadsText;
	}

	private String generateArrowPointTexts(Award award, Date startDate, Date endDate)
	{
		String arrowText = "";
		boolean isWolfElective = award.getAwardConfig().getName().equals("Wolf Electives");
		boolean isBearElective = award.getAwardConfig().getName().equals("Bear Electives");
		if (isWolfElective || isBearElective)
		{
			// we need to know how many they had before this time period, since
			// they might have partially earned some since
			int completedBefore = 0;
			int completedByTheEnd = 0;
			for (Requirement requirement : award.getRequirements())
			{
				if (requirement.getDateCompleted().before(startDate))
				{
					completedBefore++;
				}
				if (onOrBefore(requirement.getDateCompleted(), endDate))
				{
					// this will be a total of those completed by the end date
					completedByTheEnd++;
				}
			}
			// count all requirements with a number, for each 10 for each
			// arrowpoint
			int totalArrowpointCount = (completedByTheEnd / 10);
			int oldArrowpointCount = (completedBefore / 10);
			int recentArrowPointCount = totalArrowpointCount - oldArrowpointCount;
			if (recentArrowPointCount == 0)
			{
				return ""; // nothing to report
			}

			if (award.getDateCompleted() != null && onOrBetween(award.getDateCompleted(), startDate, endDate))
			{
				// it was completed during the report period
				if (totalArrowpointCount == 0 && oldArrowpointCount == 0)
				{
					// they did not track any of the requirements, they must
					// have passed them all off
					arrowText = " (1 Gold/6 Silver)";
				}
				else
				{
					// they did not track all of the requirements, assume any
					// that were not old were passed
					// off during this time period
					totalArrowpointCount = 7; // its completed
					recentArrowPointCount = totalArrowpointCount - oldArrowpointCount;
					if (oldArrowpointCount == 0)
					{
						arrowText = " (1 Gold/6 Silver)";
					}
					else
					{
						arrowText = " (" + (recentArrowPointCount - 1) + " Silver)";
					}
				}
			}
			else if (oldArrowpointCount == 0)
			{
				if (recentArrowPointCount == 1)
				{
					// they earn a gold first, then silver
					arrowText = " (1 Gold)";
				}
				else if (recentArrowPointCount > 1)
				{
					arrowText = " (1 Gold"; // there will be some silver too
					arrowText += "/" + (recentArrowPointCount - 1) + " Silver)";
				}
			}
		}
		return arrowText;
	}

	private boolean onOrAfter(Date date1, Date date2)
	{
		if (date1 == null)
			return false;
		return 0 <= date1.compareTo(date2);
	}

	private boolean onOrBefore(Date date1, Date date2)
	{
		if (date1 == null)
			return false;
		return 0 >= date1.compareTo(date2);
	}

	private boolean onOrBetween(Date questionDate, Date startDate, Date endDate)
	{
		if (questionDate == null)
			return false;
		return onOrAfter(questionDate, startDate) && onOrBefore(questionDate, endDate);
	}

	@RequestMapping("/jasper.html")
	public void jasper(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "unitId", required = true) long unitId) throws IOException, JRException
	{

		ServletContext context = request.getSession().getServletContext();

		File reportFile = new File(context.getRealPath("/reports/test1.jasper"));
		if (!reportFile.exists())
			throw new JRRuntimeException("File test1.jasper not found. The report design must be compiled first.");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("BaseDir", reportFile.getParentFile());

		JasperPrint jasperPrint = null;

		try
		{
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportFile.getPath());

			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(traxService.getScouts(unitId, "All")));
		}
		catch (JRException e)
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>JasperReports - Web Application Sample</title>");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
			out.println("</head>");

			out.println("<body bgcolor=\"white\">");

			out.println("<span class=\"bnew\">JasperReports encountered this error :</span>");
			out.println("<pre>");

			e.printStackTrace(out);

			out.println("</pre>");

			out.println("</body>");
			out.println("</html>");

			return;
		}

		if (jasperPrint != null)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			response.setContentType("application/pdf");

			net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
			response.setContentLength(baos.size());
			ServletOutputStream out1 = response.getOutputStream();
			baos.writeTo(out1);
			out1.flush();
		}
		else
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>JasperReports - Web Application Sample</title>");
			out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"../stylesheet.css\" title=\"Style\">");
			out.println("</head>");

			out.println("<body bgcolor=\"white\">");

			out.println("<span class=\"bold\">Empty response.</span>");

			out.println("</body>");
			out.println("</html>");
		}
	}
	
	/*display the page
	 * 
	 */
	@RequestMapping(value = "/toFirstClass.html", method = RequestMethod.GET)
	public ModelAndView toFirstClass(HttpServletRequest request, Map<String, Object> model)
	{
		try
		{
			model.put("htmlTables", getTrailReport(request));
		}
		catch (Exception e)
		{
			 model.put("errorMessage", "Error: "+e.getMessage()); 
		}
		return new ModelAndView("toFirstClass", model);
	}
	/*
	 * ajax call with the actual data
	 */
	@RequestMapping("/toFirstClassReport.html")
	@ResponseBody public String toFirstClassReport(HttpServletRequest request) throws Exception
	{
		String htmlString = null;
		try
		{
			htmlString = getTrailReport(request);
		}
		catch (Exception e)
		{
			Log.error("Failed to get report table: "+e.getMessage());
		}

		return htmlString;
	}

	private String getTrailReport(HttpServletRequest request) throws Exception
	{
		String htmlString = "";

		Scout selectedScout = getSelectedScout(request);

		if(selectedScout==null)
		{
			return "No Scouts Selected";
		}

		if (selectedScout.getUnit().isCub())
		{
			String tableCaption = "Bobcat to Bear Report";
			if (selectedScout.getOrganization().getHasTigers())
			{
				tableCaption = "Tiger to Bear Report";
			}
			htmlString += "<div class='scoutToFirstTableDiv'>"+generateTranslatedReportTable(request, null, tableCaption)+"</div>";
		}
		else
		{
			htmlString += "<div class='scoutToFirstTableDiv'>"+generateTranslatedReportTable(request, null, "Scout to First Class")+"</div>";
		}
	
		return htmlString;
	}

	/*
	 * { "sEcho": 1, "iTotalRecords": "57", "iTotalDisplayRecords": "57",
	 * "aaData": [ [],[],[],... ] }
	 */
	// TODO make this work to show all requirements as the row columns and all
	// boys as the header row
	@RequestMapping(value = "/showReportTable.html", method = RequestMethod.GET)
	public @ResponseBody
	String reportTable(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String htmlString = "";
		Map<Long, Long> rcCount = (Map<Long, Long>) request.getAttribute("requirementConfigIdAndCount");
		try
		{
			Award award = (Award) request.getSession().getAttribute("award");

			award.getAwardConfig();

			htmlString = generateTranslatedReportTable(request, award.getAwardConfig().getId(), award.getAwardConfig().getName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return htmlString;
	}
	
	/**put the scout names as the table column header and the requirements as the row headers
	 * @param currentAwardConfigId null if not  */
	private String generateTranslatedReportTable(HttpServletRequest request, Long currentAwardConfigId, String tableCaption) throws Exception
	{
		String htmlString = "";
		List<Scout> scouts = getScouts(request);
		String completedTd = "<td class='completed' title='Completed'>X</td>";
		String emptyTd = "<td>&nbsp;</td>";
		String canSelectTd = "<td class='completed' title='Completed'></td>";

		htmlString += "<table id='ScoutToFirstClassTable' class='awardReportTable dataTable' cellspacing='0'>\n" +
						"<caption>"+tableCaption+"</caption>"; 
				// build the header row
		htmlString += "<thead><tr>";
		htmlString += "	<th class='header' title='Requirements'></th>";
		htmlString = buildScoutHeaderHtml(htmlString, scouts);

		Map<Long, Map<Long, Set<Long>>> scoutAwardList = null;
		if (currentAwardConfigId==null)
		{
			if (tableCaption.contains("First"))
			{
				scoutAwardList = traxService.getToFirstClass(scouts);
			}
			else
			{
				scoutAwardList = traxService.getToBear(scouts);
			}
		}
		else
		{
			Set<Long> awardConfigs = new HashSet<Long>();
			awardConfigs.add(currentAwardConfigId);
			scoutAwardList = traxService.getScoutsAwardList(scouts, awardConfigs);
		}
		if (scoutAwardList == null)
		{
			return "";
		}

		htmlString += "<tbody>";
		Map<Long, Set<Long>> awardRequirementList = scoutAwardList.get(scoutAwardList.keySet().iterator().next());
		for (Long awardConfigId : awardRequirementList.keySet())
		{
			AwardConfig awardConfig = traxService.getAwardConfig(awardConfigId);
			htmlString += "<tr class='awardNameRow'><td>"+awardConfig.getName()+"</td>";
			for (Scout scout : scouts) //TODO got to be a better way
			{
				if (scout.isChecked() || scout.isSelected())
				{
					//put initials
					String[] split = scout.getFullName().split("(?<=[\\S])[\\S]*\\s*");
					htmlString += "<td title='"+scout.getFullName()+"'>"+split[0]+split[1]+"</td>";
				}
			}
			htmlString += "</tr>";

			for (RequirementConfig requirementConfig : awardConfig.getRequirementConfigs())
			{
				htmlString += "\n<tr>";
				if (requirementConfig.getCanSelect())
				{
					String text = requirementConfig.getText();
					// get max stringlen of 20
					String title = text.trim().length() < 35 ? text : text.trim().substring(0, 35); 
					htmlString += "\n<td class='rowheader' title='"+text+"'>\n" + (currentAwardConfigId==null?text:text) + "</td>";
				}
				else
				{
					htmlString += canSelectTd;
				}
				
				for (Scout scout : scouts)
				{
					if (scoutAwardList.containsKey(scout.getId()) || scout.isChecked())
					{
						// get all the award config ids for this scout
						Map<Long, Set<Long>> awardAndRequirementsMap = scoutAwardList.get(scout.getId());
						
						// they have some requirements checked off - mark that here
						Set<Long> scoutAwardConfigIds = awardAndRequirementsMap.keySet();

						// should always be true!
						if (scoutAwardConfigIds.contains(awardConfig.getId())) 
						{
							Set<Long> scoutRequirementConfigIdList = awardAndRequirementsMap.get(awardConfig.getId());
							if (requirementConfig.getCanSelect())
							{
								if (scoutRequirementConfigIdList.contains(requirementConfig.getId()))
								{
									// found the matching requirement
									htmlString += completedTd;
								}
								else
								{
									htmlString += emptyTd;
								}
							}
							else
							{
								htmlString += canSelectTd;
							}
						}
						else
						{
							// the boy does not have this award
							htmlString += emptyTd;
						}
					}
				}
				htmlString += "</tr>"; // row
			} //end of that row of requirementconfigs for all boys
		}//end of awards
		
		htmlString += "</table>"; // table
		request.getSession().setAttribute("todaysDate", new Date());
		
		return htmlString;
	}

	private String buildScoutHeaderHtml(String htmlString, List<Scout> scouts)
	{
		for (Scout scout : scouts)
		{
			if (scout.isChecked() || scout.isSelected())
			{
				String text = scout.getFullName();//requirementConfig.getText();
				// get max stringlen of 20
				String title = text;//text.trim().length() < 25 ? text : text.trim().substring(0, 25); 
				htmlString += "\n<th class='rotateheader'><div><span title='"+text+"'>\n" + title + "</span></div></th>";
			}
		}
		htmlString += "</tr></thead>"; // end row
		return htmlString;
	}
/*
	private String generateJsonReportTable(HttpServletRequest request, String htmlString, List<AwardConfig> awardConfigs) throws Exception
	{

		List<Scout> scouts = (List<Scout>) request.getSession().getAttribute("scouts");
		String completedTd = "X";
		String emptyTd = "&nbsp;";
		String canSelectTd = "";

		Map<Long, AwardConfig> awardConfigMap = new HashMap<Long, AwardConfig>();
		for (AwardConfig awardConfig : awardConfigs)
		{
			awardConfigMap.put(awardConfig.getId(), awardConfig);
		}
		Map<Long, Map<Long, Set<Long>>> scoutAwardList = traxService.getScoutsAwardList(scouts, awardConfigMap.keySet());
		if (scoutAwardList == null)
		{
			return "";
		}

		//int requirementColumnCount = getTotalRequirementCount(scoutAwardList);
		//int userRowCount = scoutAwardList.keySet().size();
		//String[][] tableArray = new String[userRowCount][requirementColumnCount];
		if (scoutAwardList != null)
		{
			Map<Long, Set<Long>> awardRequirementList = scoutAwardList.get(scoutAwardList.keySet().iterator().next());
			int index =0;
			//get headers
			for (Long awardConfigId : awardRequirementList.keySet())
			{
				AwardConfig awardConfig = awardConfigMap.get(awardConfigId);

				// for all of column 1
				htmlString += "{\n\"data\": [\n"; 
				// build the header row
				htmlString += "[\n";
				//htmlString += "\"\"";
				int rcCount=0;
				for (RequirementConfig requirementConfig : awardConfig.getRequirementConfigs())
				{
					if (requirementConfig.getCanSelect())
					{
						String text = requirementConfig.getText();
						// get max stringlen of 20
						String title = text.trim().length() < 20 ? text : text.trim().substring(0, 20); 
						htmlString += "\""+title+"\"";
					}
					else
					{
						htmlString += "\"\"";//canSelectTd;
					}
					
					if(awardConfig.getRequirementConfigs().size()<++index)
					{
						htmlString += ",\n";
					}
				}
			}
			htmlString += "],"; // end row

			//now get the data
			for (Scout scout : scouts)
			{
				if (scoutAwardList.containsKey(scout.getId()) || scout.isChecked())
				{
					htmlString += "[";
					htmlString += "\"" + scout.getFullName() + "\"";
					for (Long awardConfigId : awardRequirementList.keySet())
					{
						AwardConfig awardConfig = awardConfigMap.get(awardConfigId);
		
						if(awardRequirementList.size() < ++index)
						{
							htmlString += ",\n";
						}

						// get all the award config ids for this scout
						Map<Long, Set<Long>> awardAndRequirementsMap = scoutAwardList.get(scout.getId());

						// its possible they have earned the award, but no requirements are checked off
						// in this case we should show all requirements checked off
						boolean awardComplete = traxService.isAwardComplete(scout.getId(), awardConfigId);
						
						if (awardComplete || awardAndRequirementsMap == null)
						{
							//its either all or none - handle that here
							for (RequirementConfig rc : awardConfig.getRequirementConfigs())
							{
								if (rc.getCanSelect())
								{
									htmlString += (awardComplete) ? completedTd : emptyTd;
								}
								else
								{
									htmlString += canSelectTd;
								}
							}
						}
						else
						{
							// they have some requirements checked off - mark that here
							Set<Long> scoutAwardConfigIds = awardAndRequirementsMap.keySet();

							for (RequirementConfig rc : awardConfig.getRequirementConfigs())
							{
								// should always be true!
								if (scoutAwardConfigIds.contains(awardConfig.getId())) 
								{
									Set<Long> scoutRequirementConfigIdList = awardAndRequirementsMap.get(awardConfig.getId());
									if (rc.getCanSelect())
									{
										if (scoutRequirementConfigIdList.contains(rc.getId()))
										{
											// found the matching requirement
											htmlString += completedTd;
										}
										else
										{
											htmlString += emptyTd;
										}
									}
									else
									{
										htmlString += canSelectTd;
									}
								}
								else
								{
									// the boy does not have this award
									htmlString += emptyTd;
								}
							}
						}
						htmlString += "]\n"; // row
					}
					htmlString += "]\n"; // table
					request.getSession().setAttribute("todaysDate", new Date());
				}
				
			}
		}
		return htmlString;
	}
*/
	
	/**
	 * scout |- list of Awards |- list of requirements
	 * 
	 * @param scoutAwardList
	 * @return
	 */
	private int getTotalRequirementCount(Map<Long, Map<Long, Set<Long>>> scoutAwardList)
	{
		int totalCount = 0;
		Long firstBoyId = getFirstBoyId(scoutAwardList);
		Set<Long> awardIdList = getBoysAwardList(scoutAwardList, firstBoyId);
		for (Long awardId : awardIdList)
		{
			Set<Long> requirementIds = scoutAwardList.get(firstBoyId).get(awardId);
			totalCount += requirementIds.size();
		}
		return totalCount;
	}

	private Long getFirstBoyId(Map<Long, Map<Long, Set<Long>>> scoutAwardList)
	{
		return scoutAwardList.keySet().iterator().next();
	}

	private Set<Long> getBoysAwardList(Map<Long, Map<Long, Set<Long>>> scoutAwardList, Long firstBoyId)
	{
		return scoutAwardList.get(firstBoyId).keySet();
	}

}
