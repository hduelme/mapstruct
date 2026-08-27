package org.mapstruct.ap.test.nullcheck.jspecify.builtin;

import jakarta.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.sources.JakartaJaxbElementListProperty;
import org.mapstruct.ap.test.nullcheck.jspecify.builtin.targets.StringListProperty;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-27T01:42:24+0200",
    comments = "version: , compiler: javac, environment: Java 21.0.5 (Eclipse Adoptium)"
)
public class JakartaJaxbListMapperImpl implements JakartaJaxbListMapper {

    @Override
    public StringListProperty map(JakartaJaxbElementListProperty source) {
        if ( source == null ) {
            return null;
        }

        StringListProperty stringListProperty = new StringListProperty();

        stringListProperty.setProp( jAXBElementListToStringList( source.getProp() ) );
        stringListProperty.publicProp = jAXBElementListToStringList( source.publicProp );

        return stringListProperty;
    }

    private <T> T jaxbElemToValue( JAXBElement<T> element ) {
        if ( element == null ) {
            return null;
        }

        return element.isNil() ? null : element.getValue();
    }

    protected List<String> jAXBElementListToStringList(List<JAXBElement<String>> list) {

        List<String> list1 = new ArrayList<>( list.size() );
        for ( JAXBElement<String> jAXBElement : list ) {
            list1.add( jaxbElemToValue( jAXBElement ) );
        }

        return list1;
    }
}
