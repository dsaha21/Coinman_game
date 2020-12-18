package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class Coinman extends ApplicationAdapter {
	SpriteBatch batch;
	
	Texture background;
	Texture[] man; Texture dizzy;
	int manstate=0;
	int pause=0;
	float gravity=0.2f;

	float velocity=0;
    int manY=0;
    Random random;
    Rectangle manrec;

    ArrayList<Integer> coinsX=new ArrayList<Integer>();
	ArrayList<Integer> coinsY=new ArrayList<Integer>();
	ArrayList<Rectangle> coinrectangles=new ArrayList<Rectangle>();

	ArrayList<Integer> bombsX=new ArrayList<Integer>();
	ArrayList<Integer> bombsY=new ArrayList<Integer>();
	ArrayList<Rectangle> bombrectangles=new ArrayList<Rectangle>();

	Texture coin; Texture bomb;
	int cointcount,bombcount;

	int score =0;
	String s="GAME OVER";
	BitmapFont font;
	BitmapFont font1;
	int gamestate=0;

	@Override
	public void create () {
		batch = new SpriteBatch(); //visually on  the screen
		//img = new Texture("badlogic.jpg");
		background=new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		dizzy=new Texture("dizzy-1.png");

		manY=Gdx.graphics.getHeight()/2;
		coin=new Texture("coin.png");
		random=new Random();
		bomb=new Texture("bomb.png");
		manrec=new Rectangle();

		font =new BitmapFont();
		font1 =new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);


	}

	public void makeCoin(){
		float h= random.nextFloat() *Gdx.graphics.getHeight();
		coinsY.add((int)h);
		coinsX.add(Gdx.graphics.getWidth());
	}

	public void bombburst(){
		float h1=random.nextFloat() * Gdx.graphics.getHeight();
		bombsY.add((int)h1);
		bombsX.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		//top of background so after it

       if(gamestate==1){//LIVE
		   if(bombcount<250){bombcount++;}
		   else{
			   bombcount=0;
			   bombburst();
		   }
		   bombrectangles.clear();
		   for (int i=0; i<bombsX.size(); i++)
		   {
			   batch.draw(bomb,bombsX.get(i),bombsY.get(i));
			   bombsX.set(i,bombsX.get(i)-8);
			   bombrectangles.add(new Rectangle(bombsX.get(i), bombsY.get(i), bomb.getWidth(), bomb.getHeight()));
		   }

		   //now coins
		   if(cointcount<100){cointcount++;}
		   else{
			   cointcount=0;
			   makeCoin();
		   }
		   coinrectangles.clear();
		   for (int i=0; i<coinsX.size(); i++)
		   {
			   batch.draw(coin,coinsX.get(i),coinsY.get(i));
			   coinsX.set(i,coinsX.get(i)-4);
			   coinrectangles.add(new Rectangle(coinsX.get(i), coinsY.get(i), coin.getWidth(), coin.getHeight()));
		   }



		   if(Gdx.input.justTouched()){
			   velocity=-10;
		   }

		   if(pause<8) {pause++;
		   }else {
			   pause = 0;


			   if (manstate < 3) {
				   manstate++;
			   } else {
				   manstate = 0;
			   }
		   }

		   velocity += gravity;
		   //mans y position
		   manY -= velocity;

		   if(manY<=0)
		   { manY=0;}
	   }
       //=================================================
       	else if(gamestate==0) {
       	//waiting
		   if(Gdx.input.justTouched()){
		   	gamestate=1;
		   }
       	}
       	else if(gamestate==2){
             //GAME OVER , so to reset
		   if(Gdx.input.justTouched()){
			   gamestate=1;
			   manY=Gdx.graphics.getHeight()/2;
			   score=0;
			   velocity=0;
			   coinsY.clear();
			   coinsX.clear();
			   coinrectangles.clear();
			   cointcount=0;

			   bombsY.clear();
			   bombsX.clear();
			   bombrectangles.clear();
			   bombcount=0;
		    }
		   }

       //========================================================
		if (gamestate==2) {
             String s1="Total  SCore:";
              batch.draw(dizzy,Gdx.graphics.getWidth() / 2.0f - man[manstate].getWidth() / 2.0f, manY);
			font1.setColor(Color.BLACK);
			font1.getData().setScale(10.0f);
			font1.draw(batch,s,100,Gdx.graphics.getHeight()/2.0f);
			//font.draw(batch,s1+score,100,(Gdx.graphics.getHeight()/2.0f)-100);

		}else {
			batch.draw(man[manstate], Gdx.graphics.getWidth() / 2.0f - man[manstate].getWidth() / 2.0f, manY); //center
		  }

		//code for collision
		
		manrec=new Rectangle(Gdx.graphics.getWidth()/2.0f-man[manstate].getWidth()/2.0f,manY,man[manstate].getWidth(),man[manstate].getHeight());

		for(int i=0; i<coinrectangles.size(); i++)
		{
			if(Intersector.overlaps(manrec,coinrectangles.get(i)))
			{ //Gdx.app.log("Coins","Collision");
				score++;
				coinrectangles.remove(i);
				coinsX.remove(i);
				coinsY.remove(i);
				break;
				 }

		}

		for(int i=0; i<bombrectangles.size(); i++)
		{
			if(Intersector.overlaps(manrec,bombrectangles.get(i)))
			//Gdx.app.log("BOMBS!!","Watch out");
			{ gamestate=2;

			}
		}

		font.draw(batch,String.valueOf(score),100,200);

		batch.end(); //after finishing
	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
