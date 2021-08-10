// GENERATED CODE - DO NOT MODIFY BY HAND

// **************************************************************************
// AutoRouteGenerator
// **************************************************************************

import 'package:auto_route/auto_route.dart' as _i1;
import 'package:flutter/material.dart' as _i2;

import '../view/sign_in.dart' as _i5;
import '../view/slide_intro.dart' as _i4;
import '../view/splash_screen.dart' as _i3;

class AppRouter extends _i1.RootStackRouter {
  AppRouter([_i2.GlobalKey<_i2.NavigatorState>? navigatorKey])
      : super(navigatorKey);

  @override
  final Map<String, _i1.PageFactory> pagesMap = {
    SplashScreenRoute.name: (routeData) => _i1.CustomPage<dynamic>(
        routeData: routeData,
        builder: (_) {
          return const _i3.SplashScreen();
        },
        transitionsBuilder: _i1.TransitionsBuilders.slideLeftWithFade,
        opaque: true,
        barrierDismissible: false),
    SlideIntroRoute.name: (routeData) => _i1.CustomPage<dynamic>(
        routeData: routeData,
        builder: (_) {
          return const _i4.SlideIntro();
        },
        transitionsBuilder: _i1.TransitionsBuilders.slideLeftWithFade,
        opaque: true,
        barrierDismissible: false),
    SignInRoute.name: (routeData) => _i1.CustomPage<dynamic>(
        routeData: routeData,
        builder: (_) {
          return const _i5.SignIn();
        },
        transitionsBuilder: _i1.TransitionsBuilders.slideLeftWithFade,
        opaque: true,
        barrierDismissible: false)
  };

  @override
  List<_i1.RouteConfig> get routes => [
        _i1.RouteConfig(SplashScreenRoute.name, path: '/'),
        _i1.RouteConfig(SlideIntroRoute.name, path: '/slide-intro'),
        _i1.RouteConfig(SignInRoute.name, path: '/sign-in')
      ];
}

class SplashScreenRoute extends _i1.PageRouteInfo {
  const SplashScreenRoute() : super(name, path: '/');

  static const String name = 'SplashScreenRoute';
}

class SlideIntroRoute extends _i1.PageRouteInfo {
  const SlideIntroRoute() : super(name, path: '/slide-intro');

  static const String name = 'SlideIntroRoute';
}

class SignInRoute extends _i1.PageRouteInfo {
  const SignInRoute() : super(name, path: '/sign-in');

  static const String name = 'SignInRoute';
}
