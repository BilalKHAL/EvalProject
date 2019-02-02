// JavaScript Document

$(function() {   
  $("#custom").percircle({percent: 25,text: "Text"});
  $("#clock").percircle({perclock:true});
  $("#custom-color").percircle({progressBarColor: "#CC3366",percent: 71.5 });
  $("#countdown").percircle({perdown: true, secs: 45, timeUpText: 'finally!' });
});