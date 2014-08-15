package org.trax.form;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadItem
{
	private Long scoutId;
	@NotNull(message="Browse for a file to upload.")
	private CommonsMultipartFile fileData;

	public CommonsMultipartFile getFileData()
	{
		return fileData;
	}

	public void setFileData(CommonsMultipartFile fileData)
	{
		this.fileData = fileData;
	}

	public void setScoutId(Long scoutId)
	{
		this.scoutId = scoutId;
	}

	public Long getScoutId()
	{
		return scoutId;
	}
	
}