import javafx.application.Platform;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductBrowseControllerTest {

    @BeforeAll
    static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX already initialized
        }
    }

    @Test
    void buildSceneReturnsValidScene() {

        ProductBrowseController controller =
                new ProductBrowseController();

        Scene scene = controller.buildScene();

        assertNotNull(scene);
        assertNotNull(scene.getRoot());
    }
}
