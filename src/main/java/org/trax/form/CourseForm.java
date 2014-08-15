package org.trax.form;

import java.util.Date;

public class CourseForm extends SelectionForm
{
	Date trainingDate;
	Long courseId;
	
	public Date getTrainingDate()
	{
		return trainingDate;
	}
	public void setTrainingDate(Date trainingDate)
	{
		this.trainingDate = trainingDate;
	}
	public Long getCourseId()
	{
		return courseId;
	}
	public void setCourseId(Long courseId)
	{
		this.courseId = courseId;
	}
}
