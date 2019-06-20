<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="theme-color" content="#2196F3">
	<title>REYON WEB SERVICE</title>
	<link rel="stylesheet" href="/plugins/bootstrap/css/bootstrap.css">
	<link rel="stylesheet" href="/plugins/bootstrap-select/css/bootstrap-select.css">
	<link rel="stylesheet" href="/plugins/node-waves/waves.css">
	<link rel="stylesheet" href="/plugins/animate-css/animate.css">
	<link rel="stylesheet" href="/plugins/morrisjs/morris.css">
	<link rel="stylesheet" href="/css/style.css">
	<link rel="stylesheet" href="/css/themes/all-themes.css">
	<!-- Google Css -->
	<link href="https://fonts.googleapis.com/css?family=Lato:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic" rel="stylesheet">
	<link href="https://fonts.googleapis.com/earlyaccess/nanumgothic.css" rel="stylesheet">
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	<!-- Global site tag (gtag.js) - Google Analytics -->
	<script async src="https://www.googletagmanager.com/gtag/js?id=UA-116158631-2"></script>
	<script>
	  window.dataLayer = window.dataLayer || [];
	  function gtag(){dataLayer.push(arguments);}
	  gtag('js', new Date());
	
	  gtag('config', 'UA-116158631-2');
	</script>
	<!-- Jquery Core Js -->
	<script src="/plugins/jquery/jquery.min.js"></script>
	<!-- Bootstrap Core Js -->
	<script src="/plugins/bootstrap/js/bootstrap.js"></script>
	<!-- Select Plugin Js -->
	<script src="/plugins/bootstrap-select/js/bootstrap-select.js"></script>
	<script src="/plugins/bootstrap-select/js/i18n/defaults-ko_KR.min.js"></script>
	<!-- Slimscroll Plugin Js -->
	<script src="/plugins/jquery-slimscroll/jquery.slimscroll.js"></script>
	<!-- Waves Effect Plugin Js -->
	<script src="/plugins/node-waves/waves.js"></script>
	<!-- Jquery CountTo Plugin Js -->
	<script src="/plugins/jquery-countto/jquery.countTo.js"></script>
	<!-- Morris Plugin Js -->
	<script src="/plugins/raphael/raphael.min.js"></script>
	<script src="/plugins/morrisjs/morris.js"></script>
	<!-- Custom Js -->
	<script src="/js/admin.js"></script>
	<!-- chartjs -->
	<script type="text/javascript" src="/js/Chart.bundle.js"></script>
	<script type="text/javascript" src="/js/Chart.utils.js"></script>
	<!-- common function -->
	<script type="text/javascript" src="/js/common.js"></script>
	<script type="text/javascript">
		
		$(function() {
			vertical();
			horizontal();
			line();
			doughnut();
			pie();
			polar();
			radar();
			scatter();
		});
			
		function vertical() {
			var MONTHS = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
			var color = Chart.helpers.color;
			var barChartData = {
				labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
				datasets: [{
					label: 'Dataset 1',
					backgroundColor: color(window.chartColors.red).alpha(0.5).rgbString(),
					borderColor: window.chartColors.red,
					borderWidth: 1,
					data: [
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor()
					]
				}, {
					label: 'Dataset 2',
					backgroundColor: color(window.chartColors.blue).alpha(0.5).rgbString(),
					borderColor: window.chartColors.blue,
					borderWidth: 1,
					data: [
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor()
					]
				}]
			};
			
			var ctx = document.getElementById('vertical').getContext('2d');
			window.myBar = new Chart(ctx, {
				type: 'bar',
				data: barChartData,
				options: {
					responsive: true,
					legend: {
						position: 'top',
					},
					title: {
						display: true,
						text: 'Bar Chart'
					}
				}
			});
		}
		
		function horizontal() {
			var MONTHS = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
			var color = Chart.helpers.color;
			var horizontalBarChartData = {
				labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
				datasets: [{
					label: 'Dataset 1',
					backgroundColor: color(window.chartColors.red).alpha(0.5).rgbString(),
					borderColor: window.chartColors.red,
					borderWidth: 1,
					data: [
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor()
					]
				}, {
					label: 'Dataset 2',
					backgroundColor: color(window.chartColors.blue).alpha(0.5).rgbString(),
					borderColor: window.chartColors.blue,
					data: [
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor(),
						randomScalingFactor()
					]
				}]
			};
			
			var ctx = document.getElementById('horizontal').getContext('2d');
			window.myHorizontalBar = new Chart(ctx, {
				type: 'horizontalBar',
				data: horizontalBarChartData,
				options: {
					// Elements options apply to all of the options unless overridden in a dataset
					// In this case, we are setting the border of each horizontal bar to be 2px wide
					elements: {
						rectangle: {
							borderWidth: 2,
						}
					},
					responsive: true,
					legend: {
						position: 'right',
					},
					title: {
						display: true,
						text: 'Horizontal Bar Chart'
					}
				}
			});
		}
		
		function line() {
			var MONTHS = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
			var config = {
				type: 'line',
				data: {
					labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
					datasets: [{
						label: 'My First dataset',
						backgroundColor: window.chartColors.red,
						borderColor: window.chartColors.red,
						data: [
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor()
						],
						fill: false,
					}, {
						label: 'My Second dataset',
						fill: false,
						backgroundColor: window.chartColors.blue,
						borderColor: window.chartColors.blue,
						data: [
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor()
						],
					}]
				},
				options: {
					responsive: true,
					title: {
						display: true,
						text: 'Line Chart'
					},
					tooltips: {
						mode: 'index',
						intersect: false,
					},
					hover: {
						mode: 'nearest',
						intersect: true
					},
					scales: {
						xAxes: [{
							display: true,
							scaleLabel: {
								display: true,
								labelString: 'Month'
							}
						}],
						yAxes: [{
							display: true,
							scaleLabel: {
								display: true,
								labelString: 'Value'
							}
						}]
					}
				}
			};
			var ctx = document.getElementById('line').getContext('2d');
			window.myLine = new Chart(ctx, config);
		}
		
		function doughnut() {
			var config = {
				type: 'doughnut',
				data: {
					datasets: [{
						data: [
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
						],
						backgroundColor: [
							window.chartColors.red,
							window.chartColors.orange,
							window.chartColors.yellow,
							window.chartColors.green,
							window.chartColors.blue,
						],
						label: 'Dataset 1'
					}],
					labels: [
						'Red',
						'Orange',
						'Yellow',
						'Green',
						'Blue'
					]
				},
				options: {
					responsive: true,
					legend: {
						position: 'top',
					},
					title: {
						display: true,
						text: 'Doughnut Chart'
					},
					animation: {
						animateScale: true,
						animateRotate: true
					}
				}
			};
			var ctx = document.getElementById('doughnut').getContext('2d');
			window.myDoughnut = new Chart(ctx, config);
		}
		
		function pie() {
			var config = {
				type: 'pie',
				data: {
					datasets: [{
						data: [
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
						],
						backgroundColor: [
							window.chartColors.red,
							window.chartColors.orange,
							window.chartColors.yellow,
							window.chartColors.green,
							window.chartColors.blue,
						],
						label: 'Dataset 1'
					}],
					labels: [
						'Red',
						'Orange',
						'Yellow',
						'Green',
						'Blue'
					]
				},
				options: {
					responsive: true,
					title: {
						display: true,
						text: 'Pie Chart'
					},
				}
			};
			var ctx = document.getElementById('pie').getContext('2d');
			window.myPie = new Chart(ctx, config);
		}
		
		function polar(){
			var chartColors = window.chartColors;
			var color = Chart.helpers.color;
			var config = {
				data: {
					datasets: [{
						data: [
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
						],
						backgroundColor: [
							color(chartColors.red).alpha(0.5).rgbString(),
							color(chartColors.orange).alpha(0.5).rgbString(),
							color(chartColors.yellow).alpha(0.5).rgbString(),
							color(chartColors.green).alpha(0.5).rgbString(),
							color(chartColors.blue).alpha(0.5).rgbString(),
						],
						label: 'My dataset' // for legend
					}],
					labels: [
						'Red',
						'Orange',
						'Yellow',
						'Green',
						'Blue'
					]
				},
				options: {
					responsive: true,
					legend: {
						position: 'right',
					},
					title: {
						display: true,
						text: 'Polar Area Chart'
					},
					scale: {
						ticks: {
							beginAtZero: true
						},
						reverse: false
					},
					animation: {
						animateRotate: false,
						animateScale: true
					}
				}
			};
			var ctx = document.getElementById('polar');
			window.myPolarArea = Chart.PolarArea(ctx, config);
		}
		
		function radar(){
			var color = Chart.helpers.color;
			var config = {
				type: 'radar',
				data: {
					labels: [['Eating', 'Dinner'], ['Drinking', 'Water'], 'Sleeping', ['Designing', 'Graphics'], 'Coding', 'Cycling', 'Running'],
					datasets: [{
						label: 'My First dataset',
						backgroundColor: color(window.chartColors.red).alpha(0.2).rgbString(),
						borderColor: window.chartColors.red,
						pointBackgroundColor: window.chartColors.red,
						data: [
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor()
						]
					}, {
						label: 'My Second dataset',
						backgroundColor: color(window.chartColors.blue).alpha(0.2).rgbString(),
						borderColor: window.chartColors.blue,
						pointBackgroundColor: window.chartColors.blue,
						data: [
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor(),
							randomScalingFactor()
						]
					}]
				},
				options: {
					legend: {
						position: 'top',
					},
					title: {
						display: true,
						text: 'Radar Chart'
					},
					scale: {
						ticks: {
							beginAtZero: true
						}
					}
				}
			};
			window.myRadar = new Chart(document.getElementById('radar'), config);
		}
		
		function scatter(){
			var color = Chart.helpers.color;
			var scatterChartData = {
				datasets: [{
					label: 'My First dataset',
					borderColor: window.chartColors.red,
					backgroundColor: color(window.chartColors.red).alpha(0.2).rgbString(),
					data: [{
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}]
				}, {
					label: 'My Second dataset',
					borderColor: window.chartColors.blue,
					backgroundColor: color(window.chartColors.blue).alpha(0.2).rgbString(),
					data: [{
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}, {
						x: randomScalingFactor(),
						y: randomScalingFactor(),
					}]
				}]
			};
			var ctx = document.getElementById('scatter').getContext('2d');
			window.myScatter = Chart.Scatter(ctx, {
				data: scatterChartData,
				options: {
					title: {
						display: true,
						text: 'Scatter Chart'
					},
				}
			});
		}
		
	</script>
	</head>

	<body>
		<section id="container">
			<div class="row" style="height:50px;">&nbsp;</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<canvas id="vertical"></canvas>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<canvas id="horizontal"></canvas>
				</div>
			</div>
			<div class="row" style="height:50px;">&nbsp;</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<canvas id="line"></canvas>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<canvas id="doughnut"></canvas>
				</div>
			</div>
			<div class="row" style="height:50px;">&nbsp;</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<canvas id="pie"></canvas>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<canvas id="polar"></canvas>
				</div>
			</div>
			<div class="row" style="height:50px;">&nbsp;</div>
			<div class="row">
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<canvas id="radar"></canvas>
				</div>
				<div class="col-lg-6 col-md-6 col-sm-12 col-xs-12">
					<canvas id="scatter"></canvas>
				</div>
			</div>
			<div class="row" style="height:50px;">&nbsp;</div>
		</section>

		<!--loading Popup start-->
		<%@ include file="/WEB-INF/views/include/loading.jsp"%>
		<!--loading Popup end--> 

	</body>
</html>