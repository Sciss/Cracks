package de.sciss.cracks

import de.sciss.file._
import de.sciss.fscape._
import de.sciss.fscape.gui.SimpleGUI
import de.sciss.fscape.stream.Control
import de.sciss.synth.io.{AudioFileSpec, SampleFormat}
import graph._

import scala.swing.Swing

object Cracks {
  def main(args: Array[String]): Unit = run()

  def run(): Unit = {
    val dir     = file("/")/"data"/"projects"/"Imperfect"/"cracks"/"two_bw"
//    val dirIn   = userHome / "Documents" / "devel" / "ImperfectReconstruction" / "Cracks" /
//      "src" / "main" / "resources"
    val inNums  = Seq(4, 10, 13, 17)

    val width   = 2820 // 5184
    val height  = 2820 // 2981

    val g = Graph {
      val frameSeq: Seq[GE] = inNums.zipWithIndex.map { case (inNum, idx) =>
        val fIn   = dir / f"cracks2_$inNum%02dbwSq.png"
        val fOut  = dir / s"cracks-${idx + 1}.aif"
        val imgIn = ImageFileIn(fIn, numChannels = 1)
        val sig   = imgIn * 255 // this seems to be a bug with bw images
//        RunningSum(sig).poll(44100, "sum")
        val aSpec = AudioFileSpec(numChannels = 1, sampleFormat = SampleFormat.Int8, sampleRate = 44100)
        val aOut  = AudioFileOut(fOut, aSpec, sig)
        aOut
      }
      val numFrames = width.toLong * height * inNums.size
      val framesSum = frameSeq.reduce(_ + _)
      val prog      = framesSum / numFrames

      Progress(prog, Metro(44100))
    }

    val ctrl = Control()

    Swing.onEDT {
      SimpleGUI(ctrl)
    }

    ctrl.run(g)
  }
}