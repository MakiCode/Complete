package com.maki.UnhappyDevil.Entities;

import com.maki.UnhappyDevil.Entities.Enemies.Enemy;
import com.maki.UnhappyDevil.Entities.Loot.Loot;

/**
 * this class is from players to enemies and loot that means enemies should take damage and die or players should ignore other players 
 * 
 * @author trenton
 *
 */
public class PlayerVisitor implements Visitor {
  private Player player;
  
  public PlayerVisitor(Player player) {
    super();
    this.player = player;
  }

  @Override
  public void visitPlayer(Player player) {
    //do nothing we don't care about visiting players
  }

  @Override
  public void visitEnemy(Enemy enemy) {
    enemy.takeDamage(player.getDamageDealt());
    if(enemy.isDead()) {
      player.addKillCount();
      player.addXp(enemy.getXp());
    }
  }

  @Override
  public void visitLoot(Loot loot) {
    loot.collectLoot(player);
  }

}
