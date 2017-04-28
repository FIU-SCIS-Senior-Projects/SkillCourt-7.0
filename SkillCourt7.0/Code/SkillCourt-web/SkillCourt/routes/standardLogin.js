var express = require('express');
var router = express.Router();

/* GET users listing. */
router.get('/', function (req, res) {
    res.render('standardLogin',
        {
            site: 'SkillCourt'
        });
});

module.exports = router;