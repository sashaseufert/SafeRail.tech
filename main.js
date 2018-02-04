$(document).ready(function () {
    var vid = document.getElementById('paravid'); 
    $(window).scroll(function () {
        window.requestAnimationFrame(function () {
            var top = $("body").scrollTop();
            console.log("scroll", top, top + window.innerHeight);
            
            if(window.pageYOffset < 1600) $("div#stats .imgContainer").css({ "background-position-x": 100 - Math.min(100, window.pageYOffset / 20) + "%" });

            if(window.pageYOffset < 2400) $("div#fire .imgContainer").css({ "background-position-y": Math.min(100, Math.max(0, 80 + (window.pageYOffset - $("div#fire .imgContainer").offset().top) / 5)) + "%" });

            // console.log("fire:", (10 + ($("body").scrollTop() - $("div#fire .imgContainer").offset().top)) + "%")
            $(vid).css({ "position": "fixed", "z-index": 10, "top": 100, "right": Math.min(100, window.pageYOffset*7 - 18000) , "width": "50%"});
            if (window.pageYOffset > 2000) {
                // console.log()
                var frame = Math.min(vid.duration, (window.pageYOffset - $("div#parallax-container").offset().top) / 30);
                vid.currentTime = frame;
                console.log(vid.duration, window.pageYOffset - $("div#parallax-container").offset().top);
                
            }
        })    
    });
});