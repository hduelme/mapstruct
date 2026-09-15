<#--

    Copyright MapStruct Authors.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="org.mapstruct.ap.internal.model.assignment.SetterWrapperOuter" -->
<#import "../macro/CommonMacros.ftl" as lib>
<@lib.handleExceptions>
    <@lib.nullCheckWithElseDefaultAssignment needsNullCheck=needsSourceParameterNullCheck variableName=sourceReference>
        <#if sourceLocalVarName??>
        <@includeModel object=targetType/> ${sourceLocalVarName} = <@lib.handleAssignment/>;
        </#if>
        <@lib.handleSourceReferenceNullCheck>
            <#if ext.targetBeanName?has_content>${ext.targetBeanName}.</#if>${ext.targetWriteAccessorName}<@lib.handleWrite><#if sourceLocalVarName??>${sourceLocalVarName}<#else>${sourceReference}</#if></@lib.handleWrite>;
        </@lib.handleSourceReferenceNullCheck>
    </@lib.nullCheckWithElseDefaultAssignment>
</@lib.handleExceptions>