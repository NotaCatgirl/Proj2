import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminControllerTest {

    @Test
    void testAdminControllerExists() {

        AdminController controller = new AdminController();

        assertNotNull(controller);
    }
}