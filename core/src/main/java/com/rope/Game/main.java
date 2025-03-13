package com.rope.Game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sun.tools.javac.Main;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class main extends Game{

    /*  @Override
    public void create() {
        
        mapa m = new mapa();
        m.setVisible(true);
        this.dispose();
        
    }*/
 /*@Override
    public void create() {
        // Iniciar el juego mostrando el mapa
        setScreen(new mapa(this));
    }

    @Override
    public void render() {
        super.render(); // Esto asegurará que se renderice la pantalla actual
    }

    @Override
    public void dispose() {
        super.dispose();
    }*/
    
   /* @Override
    public void create() {
        
        int opcion = 4; 
        
        switch(opcion){
        
            case 1: 
                setScreen(new Nivel1(this));
                break; 
            case 2: 
                setScreen(new Nivel2());
                break; 
            case 3: 
                setScreen(new Nivel3());
                break; 
            case 4: 
                setScreen(new Nivel4());
                break; 
        
        }
    */
    
    
   /* private SpriteBatch batch;
    private Texture backgroundImage;
    private Stage stage;
    int opcion = 0; 

    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //  fondo
        backgroundImage = new Texture("mapa.jpg");

        // Crear los botones
        createButtons();

        // setScreen(new Nivel1()); 
    }

    void SetLVl1() {
        Nivel1 n = new Nivel1(main.this);
        setScreen(n); // Cambiar a Nivel 1
    }
    
    private void createButtons() {
        //  textura del botón
        Texture buttonTexture = new Texture("boton.png");
        TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        // Botón para Nivel 1
        ImageButton nivel1 = new ImageButton(buttonDrawable);
        nivel1.setSize(70, 70);
        nivel1.setPosition(160, 200);
        nivel1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Cambiando a Nivel 1");
                //SetLVl1();
                opcion = 1; 
                System.out.println("ya");
            }
        });

        // Botón para Nivel 2
        ImageButton nivel2 = new ImageButton(buttonDrawable);
        nivel2.setSize(70, 70);
        nivel2.setPosition(400, 368);
        nivel2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Cambiando a Nivel 2");
                //setScreen(new Nivel2()); // Cambiar a Nivel 2
                opcion = 2;
                System.out.println("ya");
            }
        });

        // Botón para Nivel 3
        ImageButton nivel3 = new ImageButton(buttonDrawable);
        nivel3.setSize(70, 70);
        nivel3.setPosition(620, 500);
        nivel3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Cambiando a Nivel 3");
                //setScreen(new Nivel3()); // Cambiar a Nivel 3
                opcion = 3;
                System.out.println("ya");
            }
        });

        //BOTON DEL CUARTO NIVEL 
        ImageButton nivel4 = new ImageButton(buttonDrawable);
        nivel4.setSize(70, 70);//asignar tamaño 
        nivel4.setPosition(400, 622); // Ajusta las coordenadas para posicionar el botón

        nivel4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar a otra pantalla cuando se haga clic en el botón
                // setScreen(new Nivel4()); //CUANDO YA ESTE 
                System.out.println("cambiando a nivel 4");
                opcion = 4;
                System.out.println("ya");

            }
        });

        //BOTON DEL QUINTO NIVEL 
        ImageButton nivel5 = new ImageButton(buttonDrawable);
        nivel5.setSize(70, 70);//asignar tamaño 
        nivel5.setPosition(170, 675); // Ajusta las coordenadas para posicionar el botón

        nivel5.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Cambiar a otra pantalla cuando se haga clic en el botón
                // setScreen(new Nivel4()); //CUANDO YA ESTE 

            }
        });
        
        
        // Añadir los botones al escenario
        stage.addActor(nivel1);
        stage.addActor(nivel2);
        stage.addActor(nivel3);
        stage.addActor(nivel4);
        stage.addActor(nivel5);
    }

   
    @Override
public void render() {
    // Limpiar la pantalla
    ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

    // Dibujar el fondo
    batch.begin();
    batch.draw(backgroundImage, 0, 0);
    batch.end();

    // Actualizar y dibujar el escenario para los botones
    stage.act(Gdx.graphics.getDeltaTime());
    stage.draw();
    
    // Lógica de cambio de pantalla
    procesarCambioPantalla();
}

private void procesarCambioPantalla() {
    if (opcion != 0) {
        switch (opcion) {
            case 1:
                setScreen(new Nivel1(this));
                break;
            case 2:
                setScreen(new Nivel2());
                break;
            case 3:
                setScreen(new Nivel3());
                break;
            case 4:
                setScreen(new Nivel4());
                break;
        }
        opcion = 0; // Reiniciar la opción para evitar múltiples cambios
    }
}
    @Override
    public void dispose() {
        batch.dispose();
        backgroundImage.dispose();
        stage.dispose();
    }*/
    
    
    @Override
    public void create() {
        // Inicia en la pantalla del menú principal
        this.setScreen(new mapa(this));
    }

}
