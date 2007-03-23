package ch.qos.logback.classic.joran.action;

import org.xml.sax.Attributes;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.net.SocketAppender;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;

public class ConsolePluginAction extends Action {

  private static final String PORT_ATTR = "port";

  @Override
  public void begin(InterpretationContext ec, String name, Attributes attributes)
      throws ActionException {
    String portStr = attributes.getValue(PORT_ATTR);

    if (portStr == null) {
      addError("The ConsolePlugin configuration requires a port attribute.");
    }

    Integer port = null;
    try {
      port = Integer.valueOf(portStr);
    } catch (NumberFormatException ex) {
      addError("Port " + portStr
          + " in ConsolePlugin config is not a correct number");
    }

    LoggerContext lc = (LoggerContext)ec.getContext();
    SocketAppender appender = new SocketAppender();
    appender.setContext(lc);
    appender.setIncludeCallerData(true);
    appender.setRemoteHost("localhost");
    appender.setPort(port.intValue());
    appender.start();
    Logger root = lc.getLogger(LoggerContext.ROOT_NAME);
    root.addAppender(appender);
    
    addInfo("Sending LoggingEvents to the plugin using port " + port);
  }

  @Override
  public void end(InterpretationContext ec, String name) throws ActionException {

  }
}
