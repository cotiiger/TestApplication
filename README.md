# TestApplication
任意の画像をなぞって切り抜く実装が入っています。

https://github.com/cotiiger/TestApplication/assets/145006121/30f634c7-2862-4a0a-ae7b-27a907d0fc67

## 操作について
- なぞった範囲を覆う最小の四角形に切り抜きます。
- タップ開始地点から半径50pxの円が描画され、指を離す箇所はその中でないと切り抜かれないようになっています。

## 構成について
DrawableView
- 指でなぞった範囲に白い線を描画する
- タップ開始地点に円を描画する
- タップを検知し、座標をViewModelに送信する

ViewModel
- ビジネスロジックを持つ
- なぞった軌跡の座標を記録する
- なぞり終わった後に最小の四角形の幅と高さを計算する
- 画像の切り抜きをする
- 値はMutableStateFlowで管理