package dk.sdu.mmmi.cbse.core.main;

import Interfaces.IAI;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import data.Entity;
import data.GameData;
import data.State;
import data.World;
import dk.sdu.mmmi.cbse.core.managers.GameInputProcessor;
import dk.sdu.mmmi.cbse.enemy.Enemy;
import dk.sdu.mmmi.cbse.player.Player;
import dk.sdu.mmmi.cbse.weapon.Weapon;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import services.IPluginService;
import services.IPostProcessor;
import services.IControlService;

/**
 *
 * @author Gruppe 11
 */
public class Game implements ApplicationListener {

    private static OrthographicCamera cam;
    private final Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private World world = new World();
    private List<IPluginService> gamePlugins = new CopyOnWriteArrayList<>();
    private Lookup.Result<IPluginService> result;
    private SpriteBatch spriteBatch;
    private Sprite playerSprite;
    private Sprite backgroundSprite;
    private Sprite bulletSprite;
    private Sprite enemySprite;
    private float playerX;
    private float playerY;
    private float enemyX;
    private float enemyY;
    private float aStarX;
    private float aStarY;
    private float bulletX;
    private float bulletY;
    private float playerRadians;
    

    @Override
    public void create() {

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

        result = lookup.lookupResult(IPluginService.class);
        result.addLookupListener(lookupListener);
        result.allItems();

        for (IPluginService plugin : result.allInstances()) {
            System.out.println("Found plugin: " + plugin);
            plugin.start(gameData, world);
            gamePlugins.add(plugin);
        }

        spriteBatch = new SpriteBatch();

        String partDir[] = System.getProperty("user.dir").split("Sem4_CBSE");
        String rootDir = partDir[0] + "Sem4_CBSE";

        backgroundSprite = new Sprite(new Texture(rootDir + "/Core/src/main/java/dk/sdu/mmmi/cbse/core/main/background.png"));
        bulletSprite = new Sprite(new Texture(rootDir + "/Weapon/src/main/java/dk/sdu/mmmi/cbse/weapon/bullet4.png"));
        enemySprite = new Sprite(new Texture(rootDir + "/Enemy/src/main/java/dk/sdu/mmmi/cbse/enemy/enemy.png"));

        /**
         * sets sprites
         */
        for (Entity e : world.getEntities()) {

            // player
            if (e instanceof Player) {
                e.setSprite(new Sprite(new Texture(e.getSpritePath())));
                playerSprite = e.getSprite();
            }

            // bullet
            if (e instanceof Weapon) {
                e.setSprite(new Sprite(new Texture(e.getSpritePath())));
                bulletSprite = e.getSprite();
            }

            // enemy
            if (e instanceof Weapon) {
                e.setSprite(new Sprite(new Texture(e.getSpritePath())));
                enemySprite = e.getSprite();
            }

        }
        
         for (Entity p : world.getEntities(Enemy.class)) {
            enemyX = p.getPositionX();
            enemyY = p.getPositionY();
        }
        
         for (IAI aStar : world.getAIList()) {
             aStar.init((int)enemyX, (int)enemyY, 0, 0);
         
         }

    }

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());
        gameData.getKeys().update();

        spriteBatch.setProjectionMatrix(cam.combined);

        for (Entity p : world.getEntities(Player.class)) {
            playerX = p.getPositionX();
            playerY = p.getPositionY();
            playerRadians = (float) Math.atan2(
                    Gdx.graphics.getHeight() - Gdx.input.getY() - playerY,
                    Gdx.input.getX() - playerX
            );

            p.setRadians(playerRadians);
        }

        for (Entity p : world.getEntities()) {
            p.setPlayerX(playerX - playerSprite.getWidth() / 2);
            p.setPlayerY(playerY - playerSprite.getHeight() / 2);
        }

        
        for (Entity p : world.getEntities(Weapon.class)) {
            bulletX = p.getPositionX();
            bulletY = p.getPositionY();
        }

        update();
        draw();
    }

    private void update() {
        // Update
        for (IControlService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.execute(gameData, world);
        }

        // Post Update
        for (IPostProcessor postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.execute(gameData, world);
        }

    }

    private void draw() {
        for (Entity entity : world.getEntities()) {

        }

        float angle = playerRadians * MathUtils.radDeg;

        if (angle < 0) {
            angle += 360;
        }

        spriteBatch.begin();
        backgroundSprite.setPosition(-355, -165);
        backgroundSprite.draw(spriteBatch);

        playerSprite.setPosition(
                playerX - playerSprite.getWidth() / 2,
                playerY - playerSprite.getHeight() / 2
        );

        playerSprite.setSize(playerSprite.getWidth(), playerSprite.getHeight());
        playerSprite.setRotation(angle);
        playerSprite.setScale(0.2F);

        for (Entity b : world.getEntities(Weapon.class)) {
            bulletSprite.setPosition(b.getPositionX(), b.getPositionY());
            bulletSprite.setSize(32, 65);
            bulletSprite.draw(spriteBatch);
        }
        for (IAI aStar : world.getAIList()) {
             aStar.updateGoal((int)playerX, (int)playerY);
             List<State> path = aStar.getPath();
             for (State i : path) {
                 aStarX = i.x;
                 aStarY = i.y;
             }
         
         }
        for (Entity e : world.getEntities(Enemy.class)) {
            
            enemySprite.setPosition(aStarX, aStarY);
            float rotation = e.getRadians() * MathUtils.radDeg;

            if (rotation < 0) {
                rotation += 360;
            }

            enemySprite.setRotation(rotation);
            enemySprite.draw(spriteBatch);
        }
        for (Entity pl : world.getEntities(Player.class)) {
            playerSprite.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private Collection<? extends IControlService> getEntityProcessingServices() {
        return lookup.lookupAll(IControlService.class);
    }

    private Collection<? extends IPostProcessor> getPostEntityProcessingServices() {
        return lookup.lookupAll(IPostProcessor.class);
    }

    private final LookupListener lookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {

            Collection<? extends IPluginService> updated = result.allInstances();

            for (IPluginService us : updated) {
                // Newly installed modules
                if (!gamePlugins.contains(us)) {
                    us.start(gameData, world);
                    gamePlugins.add(us);
                }
            }

            // Stop and remove module
            for (IPluginService gs : gamePlugins) {
                if (!updated.contains(gs)) {
                    gs.stop(gameData, world);
                    gamePlugins.remove(gs);
                }
            }
        }

    };
}
