function first() {
	var cpu = document.getElementById("cpu").getContext("2d");
	myLine = new Chart(cpu).Line(lineChartData, {
		responsive : true
	});
	window.setInterval("update()", 5000);
}
var i = 0;
var myLine;
var update = function() {
	i++;
	myLine.addData([ randomScalingFactor(), randomScalingFactor() ], 6 + i);
	myLine.update();

}
var randomScalingFactor = function() {
	return Math.round(Math.random() * 100)
};

var lineChartData = {
	labels : [ 0, 1, 2, 3, 4, 5, 6 ],
	datasets : [
			{
				label : "My First dataset",
				fillColor : "rgba(220,220,220,0.2)",
				strokeColor : "rgba(220,220,220,1)",
				pointColor : "rgba(220,220,220,1)",
				pointStrokeColor : "#fff",
				pointHighlightFill : "#fff",
				pointHighlightStroke : "rgba(220,220,220,1)",
				data : [ randomScalingFactor(), randomScalingFactor(),
						randomScalingFactor(), randomScalingFactor(),
						randomScalingFactor(), randomScalingFactor(),
						randomScalingFactor() ]
			},
			{
				label : "My Second dataset",
				fillColor : "rgba(151,187,205,0.2)",
				strokeColor : "rgba(151,187,205,1)",
				pointColor : "rgba(151,187,205,1)",
				pointStrokeColor : "#fff",
				pointHighlightFill : "#fff",
				pointHighlightStroke : "rgba(151,187,205,1)",
				data : [ randomScalingFactor(), randomScalingFactor(),
						randomScalingFactor(), randomScalingFactor(),
						randomScalingFactor(), randomScalingFactor(),
						randomScalingFactor() ]
			} ]

}
