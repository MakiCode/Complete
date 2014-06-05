/**
 * Created by Huulktya on 4/7/14.
 */
var game = new Phaser.Game(800, 600, Phaser.AUTO, '', { preload: preload, create: create, update: update });
var baseEntityHeight = 200;
var baseEntityWidth = 100;

var enemyGenerator = {
    types: [1, 2],
    makeEnemy: function () {
        var type = enemyGenerator.types[Math.floor(Math.random() * enemyGenerator.types.length)];
        return {
            type: type
        };
    }
};

var bullets = [];
var enemies = [];
var allies = [];
var unitUI;
var gameMap;

function preload() {
    //Load Assets
    //Load any and all art
    game.load.image('background', 'assets/background.png');
    game.load.image('blue', 'assets/blue-ally.png');
    game.load.image('blue-shot', 'assets/blue-wave.png');
    game.load.image('cyan', 'assets/cyan-ally.png');
    game.load.image('cyan-shot', 'assets/cyan-shot.png');
    game.load.spritesheet("enemy1", "assets/enemy1-spritesheet.png", baseEntityWidth, baseEntityHeight);
    game.load.spritesheet("enemy2", "assets/enemy2-spritesheet.png", baseEntityWidth, baseEntityHeight);

}
//Most basics:
//A unit (unchanging)
//A bullet (of some kind, single image + movement)
//An enemy (spritesheet + movement)
//A map (a concept of cells ?and lanes? must be added)
//psuedo structure:
//Entity (has basic physics support, maybe some other stuff)
//Bullet
//Ally
//Enemy


function create() {
    //Create game world
    game.add.sprite(0, 0, 'background');

    var ally = game.add.sprite(50, 210, 'blue');
    game.physics.enable(ally, Phaser.Physics.ARCADE);
    ally.body.immovable = true;
    allies.push({data: {type:"blue"}, sprite:ally});

    for (var i = 0; i < 3; i++) {
        var enemy = enemyGenerator.makeEnemy();
        var enemySprite = game.add.sprite(400, 0 + 210 * i, 'enemy' + enemy.type);
        enemySprite.animations.add("twitch");
        enemySprite.animations.play('twitch', 2, true);
        game.physics.enable(enemySprite, Phaser.Physics.ARCADE);
        enemySprite.type = enemy.type;
        enemies.push({enemy: enemy, sprite: enemySprite});

    }
    //If I visualize this as a flow from method to method....

    //I need to create:
    //The tower making AddAllyUI
    //an enemy generator
    //the background (aka map)

    //What can a unitUI do?
    //it can add units to the map
    //It can translate mouse clicks + drags into unit positions
    //Should make a unit factory
}

function fireBullet(ally) {
    var allyMidPointX = ally.sprite.width/2;
    var allyMidPointY = ally.sprite.height/2;
    //We want to move you ten pixels past the front of the sprite
    //And we want to set the bullets mid point to the same y value as the shooters midpoint
    //Allowed to modify top left corner.
    var bullet = game.add.sprite(ally.sprite.x, ally.sprite.y, ally.data.type + "-shot");
    bullet.x += ally.sprite.width  + 10;
    bullet.y = allyMidPointY;
    game.physics.enable(bullet, Phaser.Physics.ARCADE);

    bullets.push({
        type: ally.type,
        sprite: bullet
    });
}

function remove(entity, entities) {
    var index = entities.indexOf(entity);
    if (index > -1) {
        entities.splice(index, 1);
    }
    entity.sprite.kill();
}
/**
 *
 * @param bullet The bullet doing the hurting
 * @param enemy The enem(y/ies) to kill
 */
function hurtEnemy(bullet, enemy) {
    alert("hurting enemies");
    //if(bullet.data.oneEnemy) {
        remove(bullet, bullets);
    //}
    remove(enemy, enemies);
}

function update() {
    enemies.forEach(function (enemy) {
        enemy.sprite.body.velocity.x = -200;
        allies.forEach(function (ally) {
            game.physics.arcade.collide(ally.sprite, enemy.sprite);
        });
    });

    if(game.input.keyboard.isDown(Phaser.Keyboard.SPACEBAR)) {
        allies.forEach(function(ally) {
           fireBullet(ally);
        });
    }

    bullets.forEach(function(bullet) {
       bullet.sprite.body.velocity.x = 50;
       enemies.forEach(function(enemy) {
           if(game.physics.arcade.overlap(bullet.sprite, enemy.sprite, hurtEnemy)) {
               alert("YAY! AN OVERLAP!");
           }
       })
    });




    //What this needs to do:
    //Update towers to attack enemies
    //Update the enemy generator
    //Update all of the bullets
    //Update all of the enemies
}

//Event driven stuff needs to handle:
//Clicking and dragging to set towers