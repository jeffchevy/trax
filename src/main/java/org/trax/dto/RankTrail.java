package org.trax.dto;

public class RankTrail 
{
		private String awardName;
		private long requirementConfigId;
		private String text;
		
		
		public RankTrail(long requirementConfigId, String awardName, String text) {
			this.awardName = awardName;
			this.requirementConfigId = requirementConfigId;
			this.text = text;
		}
		public String getAwardName() {
			return awardName;
		}
		public void setAwardName(String awardName) {
			this.awardName = awardName;
		}
		public long getRequirementConfigId() {
			return requirementConfigId;
		}
		public void setRequirementConfigId(long requirementConfigId) {
			this.requirementConfigId = requirementConfigId;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
}