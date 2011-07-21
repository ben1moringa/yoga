package org.skyscreamer.yoga.resteasy.view;

import org.dom4j.dom.DOMDocument;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.skyscreamer.yoga.mapper.MapResultMapper;
import org.skyscreamer.yoga.mapper.ResultTraverser;
import org.skyscreamer.yoga.selector.Selector;
import org.skyscreamer.yoga.selector.SelectorParser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.MessageBodyWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public abstract class AbstractSelectorMessageBodyWriter implements MessageBodyWriter<Object>,
      ApplicationContextAware
{
   protected MapResultMapper _fieldPopulator;

   @Override
   public long getSize(Object arg0, Class<?> arg1, Type arg2, Annotation[] arg3, MediaType arg4)
   {
      return -1;
   }

   @Override
   public boolean isWriteable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3)
   {
      return true;
   }

   protected Selector getSelector()
   {
      HttpServletRequest request = ResteasyProviderFactory.getContextData(HttpServletRequest.class);
      String selectorString = request.getParameter("selector");
      return SelectorParser.parseSelector( selectorString );
   }
   
   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
   {
      this._fieldPopulator = applicationContext.getBean(MapResultMapper.class);
   }
   
   protected ResultTraverser getTraverser()
   {
      return _fieldPopulator.getResultTraverser();
   }
   

   protected static void write(OutputStream output, DOMDocument domDocument) throws IOException
   {
      OutputStreamWriter out = new OutputStreamWriter(output);
      domDocument.write(out);
      out.flush();
   }


}