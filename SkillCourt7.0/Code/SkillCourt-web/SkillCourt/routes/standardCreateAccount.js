var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function (req, res) {
    res.render('standardCreateAccount',
        {
            site: 'SkillCourt'
        });
});

module.exports = router;