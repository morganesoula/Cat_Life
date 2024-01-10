<h1 align="center">
  <a href="[https://github.com/morganesoula/catlife]">
    <img src="https://github.com/morganesoula/catlife/blob/main/catlife_icone.jpg" alt="Logo" width="125" height="125">
  </a>
</h1>

<div align="center">
  <a href="https://github.com/morganesoula/catlife/issues/new?assignees=&labels=bug&template=01_BUG_REPORT.md&title=bug%3A+">Signaler un bug</a>
  ·
  <a href="https://github.com/morganesoula/catlife/issues/new?assignees=&labels=enhancement&template=02_FEATURE_REQUEST.md&title=feat%3A+">Proposer une feature</a>
  .
  <a href="https://github.com/morganesoula/catlife/discussions">Poser une question</a>
</div>
<br />

# CatLife - [V1 disponible ici](https://play.google.com/store/apps/details?id=com.msoula.catlife)

#### Table des matières
[A Propos](#a-propos)<br />
[Intégration](#intégration)<br />
[Technologies utilisées](#technologies-utilisées)<br />
[Fonctionnalités - Screenshots](#fonctionnalités---screenshots)<br />
[Support](#support)<br />
[Licence](#licence)

## A propos

<table>
  <tr>
    <td>
      CatLife est un projet open-source dédié aux amoureux des chats. Cette application Android offre un ensemble complet d'outils pour prendre soin de son animal au mieux. Avec CatLife, les propriétaires peuvent suivre facilement les soins vétérinaires, les vaccinations et les traitements médicaux grâce à un suivi vétérinaire intégré. Le calendrier permet de planifier et de gérer les rendez-vous importants pour assurer le bien-être de leur compagnon félin. 

<br /> De plus, CatLife facilite la gestion de l'alimentation et de l'hygiène du chat en proposant un inventaire pratique. Les propriétaires peuvent ainsi surveiller le stock de croquettes, de litières et d'autres fournitures essentielles pour s'assurer que leur chat ne manque de rien.

Enfin, CatLife propose une fonction de prise de notes, permettant aux utilisateurs de consigner tout
changement de comportement, d'humeur ou de santé chez leur chat. Cela offre une manière simple de
garder un œil attentif sur la santé et le bien-être général de leur animal de compagnie.
</td>
  </tr>
</table>

## Intégration

<p>Afin de profiter pleinement de ce projet open-source, vous aurez besoin d'une clé API pour le SDK Maps.</p>

## Technologies utilisées

* Ce projet Android est entièrement développé avec [Kotlin](https://kotlinlang.org/).
* L'UI est entièrement écrite avec [Jetpack Compose](https://developer.android.com/jetpack/compose).
* L'architecture de ce projet
  associe [MVVM et Clean Architecture](https://www.youtube.com/playlist?list=PLWz5rJ2EKKc8GZWCbUm3tBXKeqIi3rcVX).
* La base de donnée choisie est [SQLDelight](https://cashapp.github.io/sqldelight/2.0.0/) afin de
  faciliter la transition lors du passage à [KMP](https://kotlinlang.org/docs/multiplatform.html).
* Les [coroutines Kotlin](https://kotlinlang.org/docs/reference/coroutines/coroutines-guide.html)
  et [Flow API](https://kotlinlang.org/docs/flow.html#flows) sont utilisés pour réaliser les appels
  au réseau et les échanges avec la base de données.
* La navigation est gérée
  par [Compose Destination](https://github.com/raamcosta/compose-destinations).
* Les images au sein de l'application sont manipulées par [Coil](https://github.com/coil-kt/coil).
* Le calendrier vient d'un projet open-source [Kalendar](https://github.com/hi-manshu/Kalendar).
  Différentes PR ont été proposées.
* [Hilt](https://dagger.dev/hilt/) a été choisie pour l'injection de dépendance.
* Les tests unitaires se basent sur [JUnit 4](https://junit.org/junit4/) et les tests fonctionnels
  utilisent [Maestro](https://maestro.mobile.dev/).

## Fonctionnalités - Screenshots

<p style="text-align: center">
<img src="app/src/main/java/com/msoula/catlife/doc/screenshots/main_page_fr_light.png" alt="main page" width="45%">
<img src="app/src/main/java/com/msoula/catlife/doc/screenshots/cat_form_fr_light.png" alt="cat form" width="45%"><br />
<img src="app/src/main/java/com/msoula/catlife/doc/screenshots/cat_detail_fr_light.png" alt="cat detail" width="45%">
<img src="app/src/main/java/com/msoula/catlife/doc/screenshots/calendar_fr_light.png" alt="calendar" width="45%"><br />
<img src="app/src/main/java/com/msoula/catlife/doc/screenshots/event_detail_fr_light.png" alt="event detail" width="45%">
<img src="app/src/main/java/com/msoula/catlife/doc/screenshots/note_detail_fr_light.png" alt="note detail" width="45%"><br />
<img src="app/src/main/java/com/msoula/catlife/doc/screenshots/inventory_fr_light.png" alt="inventory" width="45%">
</p>

## Support

N'hésitez pas à me contacter à l'aide de ces deux liens:

- [GitHub discussions](https://github.com/morganesoula/CatLifeBis/discussions)
- Par e-mail soula.morgane35@gmail.com

## Licence

Ce projet est couvert par la licence MIT. </br>
Voir [LICENSE](LICENSE) pour plus d'informations.