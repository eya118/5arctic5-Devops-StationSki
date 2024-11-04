package tn.esprit.spring;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import java.util.ArrayList;
import java.util.List;
public class TestAppender extends AppenderSkeleton{
    private final List<LoggingEvent> logEvents = new ArrayList<>();
    @Override
    protected void append(LoggingEvent event) {
        logEvents.add(event);
    }

    @Override
    public void close() {
        // Rien à faire ici
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    public List<LoggingEvent> getLogEvents() {
        return logEvents;
    }
}