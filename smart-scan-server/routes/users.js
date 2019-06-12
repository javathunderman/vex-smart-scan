var express = require('express');
var router = express.Router();
var fs = require('fs');
var util = require('util');
var fs  = require("fs");
var spawn = require('child_process').spawn;
router.get('/', function(req, res, next) {
    var sku = req.query.sku;
    const { exec } = require('child_process');
    if (fs.existsSync("cached/" + sku)) {
        var stats = fs.statSync("cached/" + sku);
        var mtime = stats.mtime;
        var date = new Date();
        if((mtime-date) > -1200000) {
            res.send(fs.readFileSync("cached/"+sku,{ encoding: 'utf8' }));
        }
        else {
            exec('java -jar smartscan.jar ' + sku, (error, stdout, stderr) => {
                  if (error) {
                    console.error(`exec error: ${error}`);
                    return;
                  }
                  fs.writeFile("cached/" + sku, (`${stdout}`), function(err) {
                      if(err) {
                          return console.log(err);
                      }

                      console.log("Successful save");
                  });
                  res.send(`${stdout}`);

                  console.log(`stderr: ${stderr}`);
                });
        }

    }
    else {

    exec('java -jar smartscan.jar ' + sku, (error, stdout, stderr) => {
      if (error) {
        console.error(`exec error: ${error}`);
        return;
      }
      fs.writeFile("cached/" + sku, (`${stdout}`), function(err) {
          if(err) {
              return console.log(err);
          }

          console.log("Successful save");
      });
      res.send(`${stdout}`);

      console.log(`stderr: ${stderr}`);
    });
    }

});


module.exports = router;
