<#--

    Copyright MapStruct Authors.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="org.mapstruct.ap.internal.model.assignment.SetterWrapperOuter" -->
<#import "../macro/CommonMacros.ftl" as lib>
<@lib.handleExceptions>
    <@lib.handleSourceReferenceNullCheck>
        <#if targetVariableName??>
        <@includeModel object=targetType/> ${targetVariableName} = <@lib.handleAssignment/>;
        </#if>
        <@lib.nullCheckWithElseDefaultAssignment needsNullCheck=true variableName=targetVariableName!sourceReference>
            <#if ext.targetBeanName?has_content>${ext.targetBeanName}.</#if>${ext.targetWriteAccessorName}<@lib.handleWrite><#if targetVariableName??>${targetVariableName}<#elseif sourceLocalVarName??>${sourceLocalVarName}<#else>${sourceReference}</#if></@lib.handleWrite>;
        </@lib.nullCheckWithElseDefaultAssignment>
    </@lib.handleSourceReferenceNullCheck>
</@lib.handleExceptions>