jQuery(document).ready(function($){
    'use strict'

    // Login Ajax
    $('#login-form').submit(function (e){
        e.preventDefault();

        $.ajax({
            url: "/login",
            method: "POST",
            data: {
                username: $('#username').val(),
			    password: $('#password').val()
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

    // Update profile
    $('#updateuser').submit(function (e){
        e.preventDefault();

        console.log("test");

        $.ajax({
            url: "/updateUser",
            method: "POST",
            data: {
                username: $('#username').val(),
                avatar: $('#avatar').val(),
                oldPassword: $('#oldPassword').val(),
                newPassword: $('#newPassword').val(),
                repeatPassword: $('#repeatPassword').val(),
			    email: $('#email').val()
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