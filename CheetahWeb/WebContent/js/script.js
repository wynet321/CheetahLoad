/**
 * dennis(wynet321@163.com)
 */

function picShow(m) {
	document.getElementById("pic_content_1").style.display = "none";
	document.getElementById("pic_content_2").style.display = "none";
	document.getElementById("pic_content_3").style.display = "none";
	document.getElementById("pic_content_" + m).style.display = "block";
	document.getElementById("pic_mini_1").className = "";
	document.getElementById("pic_mini_2").className = "";
	document.getElementById("pic_mini_3").className = "";
	document.getElementById("pic_mini_" + m).className = "selected";
}

function sliderShow() {
	if (!document.getElementById && !document.getElementsByTagName)
		return false;
	var slide = document.getElementById("slider");
	var slideli = slide.getElementsByTagName("li");
	for (var i = 0; i < slideli.length; i++) {
		slideli[i].style.left += i * 940 + "px";
	}
	var prev = document.getElementById("prev"), next = document
			.getElementById("next");
	var elem = document.getElementById("slider");
	if (!elem.style.marginLeft) {
		elem.style.marginLeft = "0";
	}
	var xpos = parseInt(elem.style.marginLeft);
	next.onclick = function() {
		if (xpos > 0 || xpos <= -1880)
			return false;
		moveElement("slider", xpos -= 940, 20);
	}
	prev.onclick = function() {
		if (xpos >= 0 || xpos < -18800)
			return false;
		moveElement("slider", xpos += 940, 20);
	}
}
function moveElement(elementID, gap, interval) {
	var elem = document.getElementById(elementID);
	if (elem.movement) {
		clearTimeout(elem.movement);
	}
	var xpos = parseInt(elem.style.marginLeft);
	if (xpos == gap)
		return true;
	if (xpos > gap) {
		var glist = Math.floor((gap - xpos) / 10);
		xpos = xpos + glist;
	}
	if (xpos < gap) {
		var glist = Math.ceil((gap - xpos) / 10);
		xpos = xpos + glist;
	}
	elem.style.marginLeft = xpos + "px";
	var move = "moveElement('" + elementID + "'," + gap + "," + interval + ")";
	elem.movement = setTimeout(move, interval);
}

function menuShow(id) {
	var elem = document.getElementById(id);
	elem.className = 'fadein';
	
}
function menuHide(id) {
	var elem = document.getElementById(id);
	elem.className = '';
}