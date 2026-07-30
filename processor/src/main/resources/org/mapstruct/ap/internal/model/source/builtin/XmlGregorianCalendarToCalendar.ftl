<#--

    Copyright MapStruct Authors.

    Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="" type="org.mapstruct.ap.internal.model.SupportingMappingMethod" -->
private <@includeModel object=findType("Calendar")/> ${name}( <@includeModel object=findType("XMLGregorianCalendar")/> xcal ) {
    <@includeModel object=findType("Calendar")/> cal = <@includeModel object=findType("Calendar")/>.getInstance();
    cal.setTimeInMillis( xcal.toGregorianCalendar().getTimeInMillis() );
    return cal;
}
