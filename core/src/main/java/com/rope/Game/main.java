package com.rope.Game;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class main extends Game {
    //private SpriteBatch batch;
    //private Texture image;

    @Override
    public void create() {
        
        setScreen(new Nivel1());
       // batch = new SpriteBatch();
        //image = new Texture("libgdx.png");
    }

   /* @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(image, 140, 210);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }*/
}
