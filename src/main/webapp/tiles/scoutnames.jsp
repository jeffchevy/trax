<%@ taglib prefix="trax" uri="/WEB-INF/TraxTags.tld"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class='cell' id="scoutnames"> <!-- put cell in here so it can be hidden if no names are need like on the troop management screen -->
	<trax:selectages/>
	<trax:scoutnames name='scouts'/>
</div>
<style>
	.allscouts:first {
	}
</style>

