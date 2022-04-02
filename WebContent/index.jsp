<html>
<head>
<meta name="author" content="Tobias Liebl">
<meta name="author" content="Sven de Haan">
</head>

<jsp:include page="header.jsp" />
<!--  Das neuere Bootstrap wird für ein schöneres Carousel benötigt-->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
	crossorigin="anonymous"></script>

<body>
	<div class="container m-1">
		<h2 class="mb-3">Willkommen bei Kontochecker - behalten Sie Ihre
			Finanzen im Auge!</h2>
	</div>


	<div id="carouselExampleCaptions" class="carousel slide"
		data-bs-ride="carousel">

		<div class="carousel-inner">
			<div class="carousel-item">
				<img src="https://i.imgur.com/bZnyWaW.png" class="d-block w-100"
					alt="...">
				<div class="carousel-caption d-none d-md-block">
					<h2 class="text-primary">Kontoübersicht</h2>
					<p class="text-primary">Importieren Sie ihre eigenen
						Kontoauszüge!</p>
				</div>
			</div>
			<div class="carousel-item">
				<img src="https://i.imgur.com/444cuVR.png" class="d-block w-100"
					alt="...">
				<div class="carousel-caption d-none d-md-block">
					<h2 class="text-primary">Kategorisierung</h2>
					<p class="text-primary">Klassifizieren Sie ihre Kontoauszüge
						nach selbstgewählten Suchbegriffen!</p>
				</div>
			</div>
			<div class="carousel-item active">
				<img src="https://i.imgur.com/TsvhPIs.png" class="d-block w-100"
					alt="...">
				<div class="carousel-caption d-none d-md-block">
					<h1 class="text-dark">Kürzlich reich geworden?</h1>
					<p class="text">Behalten Sie ihre Ausgaben im Blick, damit sie
						es auch bleiben!</p>
				</div>
			</div>
		</div>
		<button class="carousel-control-prev" type="button"
			data-bs-target="#carouselExampleCaptions" data-bs-slide="prev">
			<span class="carousel-control-prev-icon" aria-hidden="true"></span> <span
				class="visually-hidden">Previous</span>
		</button>
		<button class="carousel-control-next" type="button"
			data-bs-target="#carouselExampleCaptions" data-bs-slide="next">
			<span class="carousel-control-next-icon" aria-hidden="true"></span> <span
				class="visually-hidden">Next</span>
		</button>
	</div>


</body>
</html>