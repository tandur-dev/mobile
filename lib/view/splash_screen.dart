import 'dart:async';

import 'package:auto_route/auto_route.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_svg/flutter_svg.dart';
import 'package:hexcolor/hexcolor.dart';
import 'package:tandur_mobile/routes/app_router.gr.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({Key? key}) : super(key: key);

  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    // TODO: implement initState
    super.initState();

    Timer(Duration(seconds: 3),
            () {
              AutoRouter.of(context).pushAndPopUntil(SlideIntroRoute(), predicate: (route) {
                return false;
              });
            }
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SafeArea(
        top: false,
        child: Container(
          width: double.infinity,
          height: double.infinity,
          alignment: Alignment.center,
          decoration: BoxDecoration(
            gradient: LinearGradient(
                colors: [HexColor("#7CBD1E"), HexColor("#A7D038")],
                begin: Alignment.centerLeft,
                end: Alignment.centerRight),
          ),
          child: SvgPicture.asset("assets/images/logo_splash_screen.svg"),
        ),
      ),
    );
  }
}
