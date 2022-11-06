package pl.edu.pjwstk.pjpl.scrapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;

public class JavaScriptDateSerializer extends StdSerializer<ZonedDateTime> {

    public JavaScriptDateSerializer() {
        this(null);
    }

    public JavaScriptDateSerializer(Class<ZonedDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeNumber(value.toInstant().toEpochMilli());
    }
}
