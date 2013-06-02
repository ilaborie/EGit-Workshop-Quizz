$(function () {
    var $name = $("#name");
    var $control = $name.parent();
    var $controlGroup = $control.parent();

    // Focus
    $name.focus();

    // Check name
    $name.change(function () {
        var name = $name.val();
        if (name) {
            $.ajax({
                    url: "quizz/checkName/" + name,
                    method: "POST",
                    cache: false,
                    success: function () {
                        $control.find(".help-inline").empty();
                        if ($controlGroup.hasClass("error")) {
                            $controlGroup.addClass("success");
                        }
                        $controlGroup.removeClass("error");
                    },
                    error: function () {
                        $control.find(".help-inline").html("Login already used ! choose another one");
                        $controlGroup.addClass("error");
                    }
                }
            );
        }
        else {
            $control.find(".help-inline").html("Login required !");
            $controlGroup.addClass("error");
        }
    })
})
;
