package com.maki.UnhappyDevil.Entities;

import com.maki.UnhappyDevil.Entities.Enemies.Enemy;
import com.maki.UnhappyDevil.Entities.Loot.Loot;

public interface Visitor {
  public void visitPlayer(Player player);
  public void visitEnemy(Enemy enemy);
  public void visitLoot(Loot loot);
}
