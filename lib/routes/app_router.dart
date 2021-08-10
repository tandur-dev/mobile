import 'package:auto_route/annotations.dart';
import 'package:auto_route/auto_route.dart';
import 'package:tandur_mobile/view/sign_in.dart';
import 'package:tandur_mobile/view/slide_intro.dart';
import 'package:tandur_mobile/view/splash_screen.dart';

@MaterialAutoRouter(
    routes: <AutoRoute> [
      CustomRoute(page: SplashScreen, transitionsBuilder: TransitionsBuilders.slideLeftWithFade, initial: true),
      CustomRoute(page: SlideIntro, transitionsBuilder: TransitionsBuilders.slideLeftWithFade),
      CustomRoute(page: SignIn, transitionsBuilder: TransitionsBuilders.slideLeftWithFade)
    ]
)

class $AppRouter {}