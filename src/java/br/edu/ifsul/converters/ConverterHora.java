package br.edu.ifsul.converters;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author eric_
 */
@FacesConverter(value = "converterHora")
public class ConverterHora implements Serializable, Converter {
    
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        try {
            System.out.println("Hora gesAsObject: "+string);
            Calendar retorno = Calendar.getInstance();
            retorno.setTime(sdf.parse(string));
            return retorno;
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o == null){
            return null;
        }
        Calendar obj = (Calendar) o;
        return sdf.format(obj.getTime());
    }

}
