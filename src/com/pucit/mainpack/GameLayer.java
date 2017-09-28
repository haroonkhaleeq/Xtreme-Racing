package com.pucit.mainpack;
import java.util.ArrayList;
import java.util.Random;

import org.cocos2d.actions.instant.CCCallFuncN;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor3B;
import org.cocos2d.types.ccColor4B;

import android.R;
import android.content.Context;
import android.view.MotionEvent;

public class GameLayer extends CCColorLayer
{
		
	private CCSprite player = null;
	private ArrayList<CCSprite> _traffic = null;
	private ArrayList<CCSprite> _road = null;
	protected CCLabel _labelTime = null;
	protected CCLabel _ready = null;
	protected CCLabel _labelDistance = null;
	protected CCLabel _start = null;
	protected CCLabel _end = null;
	protected CCLabel _bar = null;
	private int gameSpeed = 2;//15
	private float frameUpdateSpeed = 1;//4
	ArrayList<CCSprite> trafficToStop = null;
	int time=60;
	int distanceCovered=0;
	int b = 28;
	
	public static CCScene scene()
	{
		CCScene scene = CCScene.node();
		CCColorLayer layer = new GameLayer(ccColor4B.ccc4(255, 255, 255, 255));
		
		scene.addChild(layer);
		
		return scene;
	}
	
	protected GameLayer(ccColor4B color)
	{
		super(color);
		
		this.setIsTouchEnabled(true);
		
		_traffic = new ArrayList<CCSprite>();
		_road = new ArrayList<CCSprite>();
		
		CGSize winSize = CCDirector.sharedDirector().displaySize();
				
		CCSprite road = CCSprite.sprite("road2.png");
		road.setPosition(CGPoint.make(winSize.width/2.0f, winSize.height/2.0f));
		addChild(road);
		
		_ready = CCLabel.makeLabel("Are you Ready !!! 3, 2, 1, ... Go !!!", "DroidSans", 25);
		_ready.setColor(ccColor3B.ccWHITE);
		_ready.setPosition(winSize.width / 2.0f, winSize.height/2.0f);
		addChild(_ready);
		
		player = CCSprite.sprite("car.png");
		player.setPosition(CGPoint.ccp( winSize.width/2.0f , 0 ));
		addChild(player);		
		CCMoveTo actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp(240,150 ));
		CCSequence actions = CCSequence.actions(actionMove);
		player.runAction(actions);
		
		String t = String.valueOf(time);
		_labelTime = CCLabel.makeLabel("Time Left: " + t, "DroidSans", 28);
		_labelTime.setColor(ccColor3B.ccWHITE);
		_labelTime.setPosition(winSize.width / 2.0f, winSize.height-25);
		addChild(_labelTime);
		
		_start = CCLabel.makeLabel("Start ", "DroidSans", 22);
		_start.setColor(ccColor3B.ccWHITE);
		_start.setPosition(25, winSize.height-85);
		addChild(_start);
		
		_end = CCLabel.makeLabel(" Finish",  "DroidSans", 22);
		_end.setColor(ccColor3B.ccWHITE);
		_end.setPosition(winSize.width-32, winSize.height-85);
		addChild(_end);
		
		
//		String d = String.valueOf(distanceCovered);
//		_labelDistance = CCLabel.makeLabel("Distance Covered: " + d, "DroidSans", 32);
//		_labelDistance.setColor(ccColor3B.ccWHITE);
//		_labelDistance.setPosition(winSize.width / 2.0f, winSize.height-55);
//		addChild(_labelDistance);
		
		
		// Background Music !
		Context context = CCDirector.sharedDirector().getActivity();
		//SoundEngine.sharedEngine().preloadEffect(context, R.raw.pew_pew_lei);
		//SoundEngine.sharedEngine().playSound(context, R.raw.bg, true);
		
		this.schedule("roadLogic", frameUpdateSpeed);//1
		this.schedule("trafficLogic", frameUpdateSpeed+2);//2
		this.schedule("update");
		this.schedule("update1");
		this.schedule("timer",1);
		this.schedule("raceBar");
		
	}
	
	@Override
	public boolean ccTouchesEnded(MotionEvent event)
	{
		
		CGPoint newLocation = CCDirector.sharedDirector().convertToGL(CGPoint.ccp(event.getX(), event.getY()));
		CGPoint currLocation = player.getPosition();
		
		//CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		float offX = newLocation.x;
		float offCurr = currLocation.x;
		
		if(offX<170)
		{
			
			if(offCurr==100)
			{
				//same location....do nothing.
				return true;
				
			}
			else
			{
				
				CCMoveTo actionMove = CCMoveTo.action(1, CGPoint.ccp( 100,150 ));		
				CCSequence actions = CCSequence.actions(actionMove);
				
				player.runAction(actions);
				
			}
			
			
		}
		else if(offX>170 && offX<300)
		{
			
			if(offCurr==240)
			{
				
				//same location....do nothing.
				return true;
				
			}
			else
			{
				
				CCMoveTo actionMove = CCMoveTo.action(1, CGPoint.ccp( 240,150 ));		
				CCSequence actions = CCSequence.actions(actionMove);
				
				player.runAction(actions);
				
			}
			
		}
		else	// offX>300
		{
		
			if(offCurr==370)
			{
				
				//same location....do nothing.
				return true;
				
			}
			else
			{
				
				CCMoveTo actionMove = CCMoveTo.action(1, CGPoint.ccp( 370,150 ));		
				CCSequence actions = CCSequence.actions(actionMove);
				
				player.runAction(actions);
				
			}
			
		}
		
			
		// Drifting Sound !
		//Context context = CCDirector.sharedDirector().getActivity();
		//SoundEngine.sharedEngine().playEffect(context, R.raw.pew_pew_lei);
		
		return true;
	}
	
	
	public void roadLogic(float dt)
	{
		roadAnim();
		roadAnim2();
		leftBuildings();
		rightBuildings();
		
//		String d = String.valueOf(distanceCovered);
//		_labelDistance.setString("Distance Covered: " + d);			
		distanceCovered = distanceCovered + 1;
	}
	
	public void trafficLogic(float dt)
	{
		int min = 1;
		int max = 4;

		Random r = new Random();
		int i = r.nextInt(max - min + 1) + min;
		
		if(i==1)
		{
			addTraffic();	//add to left lane.
		}
		else if(i==2)
		{
			addTraffic2();	//add to middle lane.			
		}
		else if(i==3)
		{
			//do not add traffic....space for car to move.			
		}
		else
		{
			addTraffic3();	//add to right lane.			
		}
				
		
	}
	
	
	public void update(float dt)	//to stop traffic when collision
	{
		
		trafficToStop = new ArrayList<CCSprite>();
				
		CGRect playerRect = CGRect.make(player.getPosition().x - (player.getContentSize().width / 2.0f),
				player.getPosition().y - (player.getContentSize().height / 2.0f),
				player.getContentSize().width,
				player.getContentSize().height);
		
		int check=0;
		for (CCSprite target : _traffic)
		{
			CGRect targetRect = CGRect.make(target.getPosition().x - (target.getContentSize().width),
											target.getPosition().y - (target.getContentSize().height),
											target.getContentSize().width,
											target.getContentSize().height);
			
			if (CGRect.intersects(playerRect, targetRect))
			{
				float offY = (player.getContentSize().height/2) + (target.getContentSize().height); 
				target.setPosition(target.getPosition().x, player.getPosition().y + offY );
				
				target.stopAllActions();
							
				trafficToStop.add(target);
							
				check=1;
								
			}
		}
		
		//stop all road actions
		if(check==1)
		{
			
			for(CCSprite b: _road)
			{
				b.stopAllActions();
				
			}
			
			this.unschedule("trafficLogic");
			this.unschedule("roadLogic");			
			
		}
		
	}
	
	
	public void update1(float dt)	//to resume traffic when car is moved away
	{
		
		//resume all road actions
		
		int check=0;
		for (CCSprite car : trafficToStop)
		{
			
			float offX = car.getPosition().x;
			float PoffX = player.getPosition().x;
			
			if(PoffX != offX)
			{		
			
				CCMoveTo actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp( car.getPosition().x, 0.0f));
					
				CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "trafficMoveFinished");
				CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
				
				car.runAction(actions);
				
				trafficToStop.remove(car);
				
				check = 1;
			}
			
		}
		
		if(check==1)
		{
			for(CCSprite b: _road)
			{
				
				CCMoveTo actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp( b.getPosition().x, 0.0f));
				CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "roadMoveFinished");
				CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
				
				b.runAction(actions);
				
			}
			
			this.schedule("roadLogic", frameUpdateSpeed);//1
			this.schedule("trafficLogic", frameUpdateSpeed+2);//2
				
		}
		
		
	}
	
	public void timer(float dt)
	{
		
		if(time==58)
			this.removeChild(_ready, true);
		
		if(time < 0)
		{
			this.unschedule("trafficLogic");
			this.unschedule("roadLogic");
			
			CGSize winSize = CCDirector.sharedDirector().displaySize();
			CCMoveTo actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp(player.getPosition().x,winSize.height));
			CCSequence actions = CCSequence.actions(actionMove);
			player.runAction(actions);			
			
			
			String t;
			
			if(distanceCovered>55)
			{
				t = "Congratulations...You have won the race.";
			}
			else
			{
				t = "Sorry...You lost.";
			}
			
			_labelTime = CCLabel.makeLabel(t, "DroidSans", 25);
			_labelTime.setPosition(winSize.width / 2.0f, (winSize.height / 2.0f) + 200);
			//_labelTime.setString(t);
			addChild(_labelTime);
			
		}
		else
		{
			
			String t = String.valueOf(time);
			_labelTime.setString("Time Left: " + t); 
			time = time - 1;
			
		}
	}
	
	public void raceBar(float dt)
	{
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		if(distanceCovered>=0 && distanceCovered<=10)
		{
			b = 50;			
		}
		else if(distanceCovered>10 && distanceCovered<=20)
		{
			b = 100;
		}
		else if(distanceCovered>20 && distanceCovered<=30)
		{
			b = 170;
		}
		else if(distanceCovered>30 && distanceCovered<=40)
		{
			b = 240;
		}
		else if(distanceCovered>40 && distanceCovered<=50)
		{
			b = 310;
		}
		else if(distanceCovered>50 && distanceCovered<=55)
		{
			b = 350;
		}
		else
		{
			b = 400;
		}
		
		_bar = CCLabel.makeLabel("_______", "DroidSans", 25);
		_bar.setPosition(b,winSize.height-50);
		//_bar.setString(t);
		addChild(_bar);
		
	}
	
	public void roadAnim()
	{
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		CCSprite line = CCSprite.sprite("line.png");
		line.setPosition(CGPoint.make(170.0f, winSize.height));
		line.setAnchorPoint(CGPoint.ccp(0.5f, 1.0f));
		addChild(line);
		_road.add(line);
		
		CCMoveTo actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp( 170.0f, 0.0f));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "roadMoveFinished");
		CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
		
		line.runAction(actions);
	
	}
	
	public void roadAnim2()
	{
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		CCSprite line = CCSprite.sprite("line.png");
		line.setPosition(CGPoint.make(300.0f, winSize.height));
		line.setAnchorPoint(CGPoint.ccp(0.5f, 1.0f));
		addChild(line);
		_road.add(line);
		
		CCMoveTo actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp( 300.0f, 0.0f));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "roadMoveFinished");
		CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
		
		line.runAction(actions);
	
	}

	public void leftBuildings()
	{
		
		int min = 1;
		int max = 6;

		Random r = new Random();
		int i = r.nextInt(max - min + 1) + min;
	
		String image = "b" + i + ".png";
		
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		CCSprite building = CCSprite.sprite(image);
		building.setPosition(CGPoint.make(20.0f, winSize.height));
		building.setAnchorPoint(CGPoint.ccp(0.5f, 1.0f));
		addChild(building);
		_road.add(building);
		
		CCMoveTo actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp( 20.0f, 0.0f));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "roadMoveFinished");
		CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
		
		building.runAction(actions);
		
	}
	
	public void rightBuildings()
	{
		
		int min = 1;
		int max = 6;

		Random r = new Random();
		int i = r.nextInt(max - min + 1) + min;
	
		String image = "b" + i + ".png";
		
		CGSize winSize = CCDirector.sharedDirector().displaySize();
		
		CCSprite building = CCSprite.sprite(image);
		building.setPosition(CGPoint.make(winSize.width - 25.0f, winSize.height));
		building.setAnchorPoint(CGPoint.ccp(0.5f, 1.0f));
		addChild(building);
		_road.add(building);
		
		CCMoveTo actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp( winSize.width - 25.0f, 0.0f));
		CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "roadMoveFinished");
		CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
		
		building.runAction(actions);
		
	}
	
	public void addTraffic()
	{
		
		int min = 1;
		int max = 10;

		Random r = new Random();
		int i = r.nextInt(max - min + 1) + min;
	
		String image = "t" + i + ".png";
		
			CGSize winSize = CCDirector.sharedDirector().displaySize();
			CCSprite traffic = CCSprite.sprite(image);
			
			traffic.setPosition(CGPoint.ccp( 100 , winSize.height));
			traffic.setAnchorPoint(CGPoint.ccp(0.5f,1));
			
			addChild(traffic);
			_traffic.add(traffic);
			
			CCMoveTo actionMove=null;
			
			if( image.equals("t2.png") || image.equals("t3.png") || image.equals("t6.png") )
			{
				
				actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp( 100, 0.0f));
				
			}
			else
			{
				actionMove = CCMoveTo.action(gameSpeed+2, CGPoint.ccp( 100, 0.0f));
			}
				
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "trafficMoveFinished");
			CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
			
			traffic.runAction(actions);
			
		
	}
	
	public void addTraffic2()
	{
		int min = 1;
		int max = 10;

		Random r = new Random();
		int i = r.nextInt(max - min + 1) + min;
	
		String image = "t" + i + ".png";
		
			CGSize winSize = CCDirector.sharedDirector().displaySize();
			CCSprite traffic = CCSprite.sprite(image);
			
			traffic.setPosition(CGPoint.ccp( 240 , winSize.height));
			traffic.setAnchorPoint(CGPoint.ccp(0.5f,1));
			
			addChild(traffic);
			_traffic.add(traffic);
			
			CCMoveTo actionMove=null;
			
			if( image.equals("t2.png") || image.equals("t3.png") || image.equals("t6.png") )
			{
				
				actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp( 240, 0.0f));
				
			}
			else
			{
				actionMove = CCMoveTo.action(gameSpeed+2, CGPoint.ccp( 240, 0.0f));
			}
			
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "trafficMoveFinished");
			CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
			
			traffic.runAction(actions);
			
		
	}
	
	public void addTraffic3()
	{
		int min = 1;
		int max = 10;

		Random r = new Random();
		int i = r.nextInt(max - min + 1) + min;
	
		String image = "t" + i + ".png";
		
			CGSize winSize = CCDirector.sharedDirector().displaySize();
			CCSprite traffic = CCSprite.sprite(image);
			
			traffic.setPosition(CGPoint.ccp( 370 , winSize.height));
			traffic.setAnchorPoint(CGPoint.ccp(0.5f,1));
			
			addChild(traffic);
			_traffic.add(traffic);
			
			CCMoveTo actionMove=null;
			
			if( image.equals("t2.png") || image.equals("t3.png") || image.equals("t6.png") )
			{
				
				actionMove = CCMoveTo.action(gameSpeed, CGPoint.ccp( 370, 0.0f));
				
			}
			else
			{
				actionMove = CCMoveTo.action(gameSpeed+2, CGPoint.ccp( 370, 0.0f));
			}
			
			CCCallFuncN actionMoveDone = CCCallFuncN.action(this, "trafficMoveFinished");
			CCSequence actions = CCSequence.actions(actionMove, actionMoveDone);
			
			traffic.runAction(actions);
			
		
	}
	
	public void trafficsMoveFinished(Object sender)
	{
		CCSprite sprite = (CCSprite)sender;
		
		_traffic.remove(sprite);
		
		this.removeChild(sprite, true);
	}
	
	public void roadMoveFinished(Object sender)
	{
		CCSprite sprite = (CCSprite)sender;
		
		_road.remove(sprite);
		
		this.removeChild(sprite, true);
	}
	
}
