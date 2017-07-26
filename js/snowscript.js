function run(){

  var width = screen.width,
      height = screen.height;

  var n = 100,
      m = 12,
      degrees = 180 / Math.PI;

  var particles = d3.range(n).map(function() {
    var x = Math.random() * width,
        y = 0;
    return {
      vx: Math.random() * 2 - 1,
      vy: Math.random() * 2 - 1,
      path: d3.range(m).map(function() { return [x, y]; }),
      count: 0
    };
  });

  var svg = d3.select  ("body").append("svg")
      .attr("width", width)
      .attr("height", height);

  var g = svg.selectAll("g")
      .data(particles)
    .enter().append("g");

  var head = g.append("ellipse")
      .attr("rx", 4)
      .attr("ry", 4);

  g.append("path")
      .datum(function(d) { return d.path.slice(0, 3); })
      .attr("class", "mid");

  d3.timer(function() {
    for (var i = -1; ++i < n;) {
      var particle = particles[i],
          path = particle.path,
          dx = particle.vx,
          dy = particle.vy,
          x = path[0][0] += dx,
          y = path[0][1] += dy,
          speed = Math.sqrt(dx * dx + dy * dy),
          count = speed * 10,
          k1 = -5 - speed / 3;

      // Bounce off the walls.
      if (x < 0 || x > width) particle.vx *= -1;
      if (y < 0)particle.vy *= -1;
  		if (y > height){
      	path[0][1] = 0;
      }
    }

    head.attr("transform", headTransform);
  });

  function headTransform(d) {
    return "translate(" + d.path[0] + ")rotate(" + Math.atan2(d.vy, d.vx) * degrees + ")";
  }

  window.addEventListener('resize', function(event){
      console.log("resize");
      width = screen.width,
      height = screen.height;
  });
}