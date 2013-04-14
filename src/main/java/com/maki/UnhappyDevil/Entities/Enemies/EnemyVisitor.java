package com.maki.UnhappyDevil.Entities.Enemies;

import com.maki.UnhappyDevil.Entities.Player;
import com.maki.UnhappyDevil.Entities.Visitor;
import com.maki.UnhappyDevil.Entities.Loot.Loot;

/**
 * this class is the visitor from enemies to players and other enemies. that
 * means that this class should do damage to players and do nothing to enemies
 * 
 * @author trenton
 * 
 */
public class EnemyVisitor implements Visitor {
  private Enemy enemy;

  public EnemyVisitor(Enemy enemy) {
    this.enemy = enemy;
  }

  @Override
  public void visitPlayer(Player player) {
    player.takeDamage(enemy.getDamage());
  }

  @Override
  public void visitEnemy(Enemy enemy) {
    //Do nothing enemies don't do anything to other enemies
  }

  @Override
  public void visitLoot(Loot loot) {
    loot.getTileIAmIn().moveEntity(enemy);
  }

}
