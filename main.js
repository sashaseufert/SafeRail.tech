isMobile = /Mobi/i.test(navigator.userAgent) || /Android/i.test(navigator.userAgent);
// alert(isMobile);
if (isMobile) {
    document.body.className += " isMobile";
}
$(document).ready(function () {
    var vid = document.getElementById('paravid'); 
    $(window).scroll(function () {
        window.requestAnimationFrame(function () {
            var top = $("body").scrollTop();
            // console.log("scroll", top, top + window.innerHeight);
            
            if(window.pageYOffset < 1600) $("div#stats .imgContainer").css({ "background-position-x": 100 - Math.min(100, window.pageYOffset / 20) + "%" });

            if(window.pageYOffset < 2400) $("div#fire .imgContainer").css({ "background-position-y": Math.min(100, Math.max(0, 80 + (window.pageYOffset - $("div#fire .imgContainer").offset().top) / 5)) + "%" });

            // console.log("fire:", (10 + ($("body").scrollTop() - $("div#fire .imgContainer").offset().top)) + "%")
            if (!isMobile) $(vid).css({ "position": "fixed", "z-index": 10, "top": Math.min(100, 9500 - window.pageYOffset ), "right": Math.min(100, window.pageYOffset*7 - 18000) , "width": "50%"});
            if (window.pageYOffset > 2000) {
                // console.log()
                var frame = Math.min(vid.duration, 2+(window.pageYOffset - $("div#parallax-container").offset().top) / 250);
                vid.currentTime = frame;
                // console.log(vid.duration, window.pageYOffset - $("div#parallax-container").offset().top);
                
            }
            var nums = $("span.countingNumber");
            $.each(nums, function (i) {
                var e = $(nums[i]);
                if ($(e).attr("data-count")) {
                    var progress = Math.max(0.2, Math.min(1.5 * (window.pageYOffset - $(e).offset().top + window.innerHeight) / window.innerHeight, 1));
                    var total = parseFloat(e.attr("data-count"));
                    // console.log(total, progress);
                    $(e).text(Math.floor(total * progress));
                }    
            })
            
            // $angle = $(document.body).height() - (window.pageYOffset + window.innerHeight);
            $angle = $(document.body).height() - (window.pageYOffset + window.innerHeight);
            console.log($(document.body).height() - (window.pageYOffset + window.innerHeight));
            $("#meh").css({"transform" : "rotate(" + $angle*3 +"deg)", "left" : $angle*3 + "px", "bottom": Math.sin($angle/10)*50+Math.abs(Math.tan($angle/50)*100) + "px"});
        })    
    });
});


