jQuery(document).ready(function($){
    'use strict'

    // Login Ajax
    $('#login-form').submit(function (e){
        e.preventDefault();
        
        console.log("TEST!");

        $.ajax({
            url: "/login",
            method: "POST",
            data: {
                username: "admin2",
                password: "admin"
            },
            success: function(data){
                window.location.replace(data);
                console.log(data)
            },
            fail: function(data){
                console.log(data);
                window.location.href = "error.html";
            }		
        });
    });
});