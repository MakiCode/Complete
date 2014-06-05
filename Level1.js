/**
 * Created by Huulktya on 4/8/14.
 */
Game.Level1 = function () {

};

Game.Level1.prototype.Data = {
    Enemies: { // todo Load this
        enemySequence: [0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 1, 1],
        typeNames: ["enemy1", "enemy2"],
        types: { //todo And this
            "enemy1": {
                difficulty: 1,
                hp: 5,
                width: 100,
                height: 200
            },
            "enemy2": {
                difficulty: 2,
                hp: 5,
                width: 100,
                height: 200
            }
        },
        difficulty: [
            ["enemy1"],
            ["enemy2"]
        ]
    },
    Allies: { //todo And this
        typeNames: ["blue", "cyan"],
        types: {
            "blue": {
                cost: 2,
                producer: true,
                hp: 3,
                width: 100,
                height: 200
            },
            "cyan": {
                cost: 3,
                producer: false,
                hp: 4,
                width: 100,
                height: 200
            }
        }
    },
    Weapons: { //todo And this from json
        typeNames: ["blue", "cyan"],
        types: {
            "blue": {
                damage: 0,
                effects: {
                }
            },
            "cyan": {
                damage: 1,
                effects: {
                }
            }
        }
    }
};

Game.Level1.prototype = {
    startTime: 0,
    Bullets: {
        Bullets: Game.Level1.prototype.Data.Weapons,
        bullets: [],
        updateBullets: function (game) {
            this.bullets.forEach(function (bullet, indexB, arrayB) {
                if (Game.Level1.prototype.EnemyGenerator.enemiesByLane[bullet.typeData.y]) {
                    Game.Level1.prototype.EnemyGenerator.enemiesByLane[bullet.typeData.y].every(function (enemy, index, array) {
                        if (game.physics.arcade.overlap(bullet, enemy)) {
                            enemy.typeData.takeDamage(bullet.typeData.damage);
                            if (enemy.typeData.hp <= 0) {
                                enemy.typeData.isDead = true;
                                array.splice(index, 1);
                                var index2 = Game.Level1.prototype.EnemyGenerator.enemiesByLane[enemy.typeData.y].indexOf(enemy);
                                Game.Level1.prototype.EnemyGenerator.enemiesByLane[enemy.typeData.y].splice(index2, 1);
                                enemy.destroy();
                            }
                            arrayB.splice(indexB, 1);
                            bullet.destroy();
                            return false;
                        }
                    });
                }
            });
        },
        makeBasicBullet: function (y, type) {
            var object = this.Bullets.types[type];
            return {
                type: type,
                y: y,
                damage: object.damage

            }
        },
        makeBullet: function (game, ally) {
            var scale = Game.Level1.prototype.Map.scale;
            var x = ally.x + scale; //+ scale to move them to the front of the ally
            var y = ally.y;
            var bulletSprite = game.add.sprite(x, y, ally.typeData.type + "-shot");
            game.physics.arcade.enable(bulletSprite);
            bulletSprite.body.velocity.x = 200;
            bulletSprite.z = 900000;
            bulletSprite.checkWorldBounds = true;
            bulletSprite.typeData = this.makeBasicBullet(ally.typeData.y, ally.typeData.type);
            bulletSprite.events.onOutOfBounds.add(this.remove, this);

            this.bullets.push(bulletSprite);
        },
        remove: function (bullet) {
            this.bullets.splice(this.bullets.indexOf(bullet), 1);
            bullet.kill();
        }
    },
    EnemyGenerator: {
        enemyGroup: null,
        Enemies: Game.Level1.prototype.Data.Enemies,
        //Has a sprite with a data type object
        enemies: [],
        currentPosInSequence: 0,
        enemiesByLane: [
            [],
            [],
            [],
            [],
            [],
            []
        ],
        allDead: function () {
            return this.enemies.length == 0;
        },
        isEmpty: function () {
            return this.currentPosInSequence >= this.Enemies.enemySequence.length;
        },
        isOver: function () {
            return this.isEmpty() && this.allDead();
        },
        updateEnemies: function (game) {
            var allies = Game.Level1.prototype.Map.alliesGroupByLane;
            this.enemies.forEach(function (enemy) {
                if (!game.physics.arcade.overlap(enemy, allies[enemy.typeData.y])) { //Broken, need to only compare to allies in lane
                    enemy.typeData.isStopped = false;
                    enemy.body.velocity.x = -100;
                }
            });
        },
        /**
         * Adds up the difficulty of the enemies in a lane and returns it.
         * @param i the lane to examine
         * @returns {number} the difficulty of the lane as calculated above
         */
        sumUpEnemyDifficultyInLane: function (i) {
            var total = 0;
            for (var j = 0; j < this.enemiesByLane[i].length; j++) {
                total += this.enemiesByLane[i][j].difficulty;
            }
            return total;
        },
        /**
         * Gets the best lanes available by choosing the one with the least enemies or a random lanes
         * @returns {number} an int representing the y value of the lanes in mapUnits
         */
        getBestY: function () {
            //Choose the lanes with the lowest difficulty score, if equal, choose a random one
            var min = {
                lanes: [],
                min: -1
            };
            for (var i = 0; i < this.enemiesByLane.length; i++) {
                var difficulty = this.sumUpEnemyDifficultyInLane(i);
                if (difficulty < min.min || min.min == -1) {
                    min.lanes = [i];
                    min.min = difficulty;
                } else if (difficulty == min.min) {
                    min.lanes.push(i)
                }
            }
            return min.lanes[Math.floor(Math.random() * min.lanes.length)];
        },
        /**
         * Generates an enemy if the proper times has been reached
         */
        makeEnemy: function (game) {
            var timestamp = game.time.now;
            if ((timestamp % 10000 <= 30 && timestamp % 10000 >= 0) && this.currentPosInSequence < this.Enemies.enemySequence.length) { //Need to think of the best way to make this more dynamic
                if (this.enemyGroup == null) {
                    this.enemyGroup = game.add.group();
                }
                var difficulty = this.Enemies.enemySequence[this.currentPosInSequence];
                var enemyChoices = this.Enemies.difficulty[difficulty];

                var enemy = enemyChoices[Math.floor(Math.random() * enemyChoices.length)];
                var y = this.getBestY();
                enemy = this.makeEnemyFromType(enemy, y);
                this.currentPosInSequence++;
                var scale = Game.Level1.prototype.Map.scale;
                //This is way to high,
                var pixelY = ((y * scale) + scale) - game.cache.getImage(enemy.type).height;
                var enemySprite = this.enemyGroup.create(maxXVal, pixelY + Game.Level1.prototype.yPlayingField, enemy.type);
                game.physics.arcade.enable(enemySprite);
                enemySprite.body.velocity.x = -100;
                enemySprite.z = 100000;
                enemySprite.checkWorldBounds = true;
                enemySprite.typeData = enemy;
                enemySprite.events.onOutOfBounds.add(Game.Level1.prototype.endGame, this);

                this.enemies.push(enemySprite);
                this.enemiesByLane[y].push(enemySprite);

            }
        },
        /**
         *Make an enemy from the given type and Y Value
         * @param type The type of an enemy
         * @param y The y position in map units (x doesn't matter)
         * @returns {{type: *, y: *, hp: number, difficulty: number}}
         */
        makeEnemyFromType: function (type, y) {
            var object = this.Enemies.types[type];
            return {
                type: type,
                y: y,
                hp: object.hp,
                difficulty: object.difficulty,
                takeDamage: function (damage) {
                    if (damage) {
                        this.hp -= damage;
                    } else {
                        this.hp -= 1;
                    }
                }
            }
        },
        remove: function (enemy) {
            var index = this.enemies.indexOf(enemy);
            this.enemies.splice(index, 1);
            index = this.enemiesByLane[enemy.typeData.y].indexOf(enemy);
            this.enemiesByLane[enemy.typeData.y].splice(index, 1);
            enemy.kill();
        }
    },
    Map: {
        scale: 75, //The scale difference between mapUnits and pixels
        alliesGroupByLane: [],
        alliesGroup: null,
        alliesMap: [
            []
        ],
        /**
         * A list of the format
         * [
         *   {
         *     x:x
         *     y:y
         *     ally:ally
         *   },
         *   ...
         * ]
         */
        alliesList: [],

        /**
         * Adds the ally at the specified x and y value to the map data
         * @param x The allies x value (in mapUnits)
         * @param y The allies y value (in mapUnits)
         * @param ally The ally to add at the given position
         * @param game A game object (for creating groups)
         */
        addAlly: function (x, y, ally, game) {
            if (this.alliesGroup == null) {
                this.alliesGroup = game.add.group();
            }
            if (!this.alliesMap[y]) {
                this.alliesMap[y] = []
            }
            if (!this.alliesGroupByLane[y]) {
                this.alliesGroupByLane[y] = game.add.group();
            }
            this.alliesMap[y][x] = ally;
            this.alliesList.push({x: x, y: y, ally: ally});
            this.alliesGroup.add(ally);
            this.alliesGroupByLane[y].add(ally)
        },
        convertXToPixels: function (x) {
            return x * this.scale;
        },
        convertYToPixels: function (y) {
            return y * this.scale;
        },
        convertXToMapUnits: function (x) {
            var xPF = Game.Level1.prototype.xPlayingField;
            if (x < xPF) return 0;
            var newX = x - xPF;
            return Math.floor(newX / this.scale);
        },
        convertYToMapUnits: function (y) {
            var yPF = Game.Level1.prototype.yPlayingField;
            if (y < yPF) return 0;
            var newY = y - yPF;
            return Math.floor(newY / this.scale);
        },
        isaAvailable: function (x, y) {
            if (!this.alliesMap[y]) {
                return true;
            } else {
                return !this.alliesMap[y][x];
            }
        },
        updateAllies: function (game) {
            var Enemies = Game.Level1.prototype.EnemyGenerator;
            this.alliesList.forEach(function (ally, index, array) {
                if (ally.ally.typeData.dead == true) {
                    //Do something more interesting in final version
                    if (ally.ally.typeData.producer) {
                        game.time.events.remove(ally.ally.typeData.timer);
                    }
                    ally.ally.destroy();
                    array.splice(index, 1);
                    Game.Level1.prototype.Map.alliesMap[ally.y][ally.x] = null; //splice collapses the array.
                } else {
                    if (Enemies.enemiesByLane[ally.y].length > 0) {
                        ally.ally.typeData.fire(game, ally.ally);
                    }
                    Enemies.enemiesByLane[ally.y].forEach(function (enemy) {
                        if (game.physics.arcade.overlap(ally.ally, enemy)) {
                            var time = game.time;
                            if (!enemy.typeData.isStopped) {
                                enemy.typeData.isStopped = true;
                                enemy.body.velocity.x = 0;
                                enemy.typeData.lastHit = time.now;
                                ally.ally.typeData.takeDamage();
                            } else {
                                if (time.elapsedSecondsSince(enemy.typeData.lastHit) > 1) {
                                    enemy.typeData.lastHit = time.now;
                                    ally.ally.typeData.takeDamage();
                                }
                            }
                        }
                    });
                }
            });
        }
    },
    AddAllyUI: {
        Allies: Game.Level1.prototype.Data.Allies,
        alliesInMenu: [],
        resources: 3,
        collectResource: function (resource) {
            resource.destroy();
            this.resources += 1;
        },
        produce: function (ally, game) {
            var resource = game.add.sprite(ally.x, ally.y, "resource");
            resource.x += ally.width / 2;
            resource.x -= resource.width / 2;
            resource.y += ally.height / 2;
            resource.y -= resource.height / 2;
            resource.inputEnabled = true;
            resource.events.onInputDown.add(this.collectResource, this)
        },
        /**
         * Generates an ally of the given types
         *
         * @param type A string key of the ally to generate
         * @param x The x value of the ally (in mapUnits)
         * @param y The y value of the ally (in mapUnits)
         * @returns {{type: *, hp: number, x: *, y: *}}
         */
        makeAlly: function (type, x, y) {
            var object = this.Allies.types[type];
            return {
                producer: object.producer,
                type: type,
                hp: object.hp,
                x: x,
                y: y,
                dead: false,
                lastFired: null,
                takeDamage: function (damage) {
                    if (damage) {
                        this.hp -= damage;
                    } else {
                        this.hp -= 1;
                    }
                    if (this.hp <= 0) {
                        this.dead = true;
                    }
                },
                fire: function (game, allySprite) {
                    if (!this.producer) {
                        if (!this.lastFired) {
                            this.lastFired = game.time.now;
                            Game.Level1.prototype.Bullets.makeBullet(game, allySprite);
                        } else if (game.time.elapsedSecondsSince(this.lastFired) > 2) {
                            Game.Level1.prototype.Bullets.makeBullet(game, allySprite);
                            this.lastFired = game.time.now;
                        }
                    }
                }
            };
        },
        /**
         * Creates the AddAllyUI for adding allies to the board
         *
         * @param game The game object
         * @param x The x value (in pixels) of the top left corner of the AddAllyUI frame
         * @param y The y value (in pixels) of the top left corner of the AddAllyUI frame
         */
        makeUI: function (game, x, y) {
            var lastXUI = x;
            var i = 0;
            //Make resources display
            var b = game.add.sprite(lastXUI, y, 'resource-background');
            var tb = game.add.sprite(lastXUI, y + b.height, 'resource-textbar');
            tb.y -= tb.height;
            var r = game.add.sprite(lastXUI, y, 'resource');
            r.x += tb.width / 2;
            r.x -= r.width / 2;
            r.y += tb.height / 2;

            this.text = game.add.text(tb.x, tb.y, this.resources + "");
            lastXUI += b.width;
            var lastXSprite = lastXUI;

            for (var key in this.Allies.types) {
                lastXUI += game.add.sprite(lastXUI, y, 'ui-background').width;
                var ally = game.add.sprite(lastXSprite + this.X_SPACING, y + this.TOP_OFFSET, key + "-card");
                lastXSprite = ally.x + ally.width;
                ally.inputEnabled = true;
                ally.typeData = {};
                ally.typeData.producer = this.Allies.types[key].producer;
                ally.typeData.cost = this.Allies.types[key].cost;
                ally.type = key;
                ally.events.onInputDown.add(this.setLastClickedAlly, this);
                this.alliesInMenu[i] = ally;
            }

            game.add.sprite(lastXUI, y, 'cap');
            var health = game.add.sprite(game.stage.bounds.width, y, 'health-area');
            health.x -= health.width;
            this.healthArea = game.add.text(game.stage.bounds.width - health.width + 40, y + 40, Game.Level1.prototype.playerHealth + "", {size: "3em"});
        },
        healthArea: null,
        makeLastClickedAlly: function (x, y, game) {
            var map = Game.Level1.prototype.Map;
            var xPF = Game.Level1.prototype.xPlayingField;
            var yPF = Game.Level1.prototype.yPlayingField;
            if (this.lastClickedAlly != null) {
                var newX = map.convertXToMapUnits(x);
                var newY = map.convertYToMapUnits(y);
                if (map.isaAvailable(newX, newY) && this.resources >= this.lastClickedAlly.typeData.cost) {
                    var allyData = this.makeAlly(this.lastClickedAlly.type, newX, newY);
                    //console.dir(allyData);
                    var ally = game.add.sprite(map.convertXToPixels(newX) + xPF, map.convertYToPixels(newY) + yPF, this.lastClickedAlly.type);
                    game.physics.arcade.enable(ally);
                    ally.body.immovable = true;
                    ally.typeData = allyData;//Need to repeat this everywhere
                    map.addAlly(newX, newY, ally, game);

                    if (allyData.producer == true) {
                        var timer = game.time.events.loop(Phaser.Timer.SECOND * 3, this.produce, this, ally, game);
                        ally.typeData.timer = timer;
                    }
                    this.resources -= this.lastClickedAlly.typeData.cost;
                }
            }
        },
        text: null,
        TOP_OFFSET: 10,
        X_SPACING: 10,
        setLastClickedAlly: function (ally) {
            this.lastClickedAlly = ally;
        },
        lastClickedAlly: null,
        highlightSprite: null,
        /**
         * Highlights the unit the user last clicked on
         * @param game The game object to use
         */
        updateUI: function (game) {
            this.text.setText(this.resources);
            this.healthArea.setText(Game.Level1.prototype.playerHealth);
            if (this.highlightSprite == null) {
                this.highlightSprite = game.add.sprite(-90000, -90000, "marker");
            }
            if (this.lastClickedAlly != null) {
                var x = this.lastClickedAlly.x;
                var y = this.lastClickedAlly.y;
                this.highlightSprite.x = x;
                this.highlightSprite.y = y;
            }
        }
    },
    xPlayingField: 100,
    yPlayingField: 117,
    create: function () {
        this.add.sprite(0, 0, "background");
        var field = this.add.sprite(this.xPlayingField, this.yPlayingField, "field");
        field.inputEnabled = true;
        field.events.onInputDown.add(this.plantAlly, this);
        this.AddAllyUI.makeUI(this, 0, 0);

    },
    plantAlly: function (sprite, pointer) {
        this.AddAllyUI.makeLastClickedAlly(pointer.x, pointer.y, this);
    },
    update: function () {
        this.EnemyGenerator.makeEnemy(this);
        this.AddAllyUI.updateUI(this);
        this.Map.updateAllies(this);
        this.EnemyGenerator.updateEnemies(this);
        if (this.renderGroup == null && this.Map.alliesGroup != null && this.EnemyGenerator.enemyGroup != null) {
            this.renderGroup = this.add.group();
            this.renderGroup.add(this.EnemyGenerator.enemyGroup);
            this.renderGroup.add(this.Map.alliesGroup);
        }
        this.Bullets.updateBullets(this);
        if (this.playerHealth <= 0) {
            this.state.start("Lose");

        } else if (this.EnemyGenerator.isOver()) {
            this.state.start("Win");
        }
        console.log(this.EnemyGenerator.isOver());
    },
    renderGroup: null,
    playerHealth: 4,
    endGame: function (enemy) {
        Game.Level1.prototype.playerHealth -= 1;
        Game.Level1.prototype.EnemyGenerator.remove(enemy);
    }
};


//Do these update step.

//TODO
//Win/Loss detection
//Enemy Spawning imporvements
//TODO
//Improve the opening (enemies can't spawn until after a certain time)
//mutiple levels
//More allies
//More enemies
//Variey!

//TODO bugs:
//Might need to double check the enemy generation (seems to have a 0 lane bias, and doubles up enemies)