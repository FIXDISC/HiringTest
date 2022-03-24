import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EchoTest {
    @Test
    void connect() throws Exception{
        Echo.connect("https://postman-echo.com/post","hi there");
    }
}