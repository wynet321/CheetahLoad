var AxisX = 0;
var chart;
var initializeChart = function() {
	//chart = getChart();
	generateChart();
}
var generateChart = function() {
	var lineData = {
		labels : [],
		datasets : [ {
			label : "CPU",
			fillColor : "rgba(220,220,220,0.2)",
			strokeColor : "rgba(220,220,220,1)",
			pointColor : "rgba(220,220,220,1)",
			pointStrokeColor : "#fff",
			pointHighlightFill : "#fff",
			pointHighlightStroke : "rgba(220,220,220,1)",
			data : []
		} ]
	};

	var cpu = document.getElementById("cpu").getContext("2d");
	var myLine = new Chart(cpu).Line(lineData, {
		responsive : true,
		bezierCurve : false,
		pointDot : false,
		scaleShowGridLines : false
	});
	var time = window.setInterval("updateChart()", 2000);
	chart = {
		cpu : [{ "chart":myLine, "time":time,"test":1} ]
	};
}

var stopUpdate = function(chart) {
	clearTimeout(chart.cpu[0].time);
}

var getXHR = function() {
	var xhttp;
	if (window.XMLHttpRequest) {
		xhttp = new XMLHttpRequest();
	} else {
		// code for IE6, IE5
		xhttp = new ActiveXObject("Microsoft.XMLHTTP");
	}
	return xhttp;
}
var updateChart = function(chart) {
	var xhttp = getXHR();
	xhttp.open("GET", "/CheetahWeb/servlet/getData", true);
	xhttp.setRequestHeader("If-Modified-Since", "0");
	xhttp.send();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			var data = xhttp.responseText;
			var content = JSON.parse(data);
			var increment = 0;
			while (content.data[j]) {
				chart.cpu[0].chart.addData([ content.data[increment] ], AxisX
						+ increment);
				increment++;
			}
			AxisX += increment;
			chart.cpu[0].chart.update();
		}
	}

}