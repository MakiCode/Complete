/**
 * Created by Huulktya on 4/10/14.
 */

Game.Win = function () {

};

Game.Win.prototype = {
    create: function () {
        this.add.sprite(0,0,"win");
        this.time.events.add(Phaser.Timer.SECOND * 10, this.restart, this);
    },
    restart:function() {
        this.state.start("Level1")
    }
};

