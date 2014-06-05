/**
 * Created by Huulktya on 4/10/14.
 */

Game.Lose = function () {

};

Game.Lose.prototype = {
    create: function () {
        this.add.sprite(0,0,"lose");
        this.time.events.add(Phaser.Timer.SECOND * 10, this.restart, this);
    },
    restart:function() {
        this.state.start("Level1")
    }
};

