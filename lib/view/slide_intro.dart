import 'package:auto_route/auto_route.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:hexcolor/hexcolor.dart';
import 'package:intro_slider/intro_slider.dart';
import 'package:intro_slider/slide_object.dart';
import 'package:tandur_mobile/routes/app_router.gr.dart';

class SlideIntro extends StatefulWidget {
  const SlideIntro({Key? key}) : super(key: key);

  @override
  _SlideIntroState createState() => _SlideIntroState();
}

class _SlideIntroState extends State<SlideIntro> {
  List<Slide> slides = [];

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    slides.add(
      new Slide(
        backgroundImage: "assets/images/first_slide.png",
        backgroundBlendMode: BlendMode.lighten
      ),
    );
    slides.add(
      new Slide(
          backgroundImage: "assets/images/second_slide.png",
          backgroundBlendMode: BlendMode.lighten
      ),
    );
    slides.add(
      new Slide(
          backgroundImage: "assets/images/third_slide.png",
          backgroundBlendMode: BlendMode.lighten
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return new IntroSlider(
      slides: this.slides,
      onDonePress: () {
        AutoRouter.of(context).pushAndPopUntil(SignInRoute(), predicate: (route) {
          return false;
        });
      },
      renderSkipBtn: Text(
        "Lewati",
        style: TextStyle(
          color: Colors.black
        ),
      ),
      renderNextBtn: Text(
        "Selanjutnya",
        style: TextStyle(
            color: HexColor("#7BBE42")
        ),
      ),
      renderDoneBtn: Text(
        "Selanjutnya",
        style: TextStyle(
            color: HexColor("#7BBE42")
        ),
      ),
      colorDot: HexColor("#E5E5E5"),
      colorActiveDot: HexColor("#7BBE42"),
    );
  }
}
