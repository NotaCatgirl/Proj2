import javafx.application.Platform;
import javafx.scene.Scene;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SceneFactoryTest {

    @BeforeAll
    static void setUpJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException javaFxAlreadyStarted) {
        }
    }

    @Test
    void registerSceneIsNotNull() {
        Scene registerScene = SceneFactory.createScene(SceneType.REGISTER);
        assertNotNull(registerScene);
    }

    @Test
    void registerSceneHasCorrectSize() {
        Scene registerScene = SceneFactory.createScene(SceneType.REGISTER);
        assertEquals(800, registerScene.getWidth());
        assertEquals(600, registerScene.getHeight());
    }

    @Test
    void placeholderScenesAreCreated() {
        Scene productBrowseScene = SceneFactory.createScene(SceneType.PRODUCT_BROWSE);
        Scene cartScene = SceneFactory.createScene(SceneType.CART);
        Scene adminScene = SceneFactory.createScene(SceneType.ADMIN);

        assertNotNull(productBrowseScene);
        assertNotNull(cartScene);
        assertNotNull(adminScene);
    }
}