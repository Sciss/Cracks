val width  = 5184
val height = 2981
val b      = Buffer("buf")

val cx: GE = "cx".kr(width  * 0.5) + SinOsc.ar("cxmf".kr(0)) * "cxmd".kr(0)
val cy: GE = "cy".kr(height * 0.5) + SinOsc.ar("cymf".kr(0)) * "cymd".kr(0)
val rx: GE = "rx".kr((width  - 1) * 0.5) + SinOsc.ar("rxmf".kr(0)) * "rxmd".kr(0)
val ry: GE = "ry".kr((height - 1) * 0.5) + SinOsc.ar("rymf".kr(0)) * "rymd".kr(0)
val freq: GE = "freq".kr(1.0) + SinOsc.ar("fmf".kr(0)) * "fmd".kr(0)
val twopi = math.Pi * 2
val phase = Phasor.ar(speed = twopi * 2 * freq / SampleRate.ir, lo = 0, hi = twopi)

val x = (cx + rx * phase.cos).fold(0, width )
val y = (cy + ry * phase.sin).fold(0, height)

val pos = y.roundTo(1) * width + x

val read = BufRd.ar(numChannels = 1, buf = b, index = pos, loop = 1, interp = 0 /* 2 */)

val sig = LeakDC.ar(read)

Out.ar(0, Pan2.ar(sig, "pan".kr(0)))
