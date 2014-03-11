# coding: utf-8

require "sinatra"
require "mail"
require "cairo"
require "rmagick"
require "json"
require "Base64"

family = "金城"
name = "廣太"
rubi_family = "Kinjo"
rubi_name = "Kodai"
school = "国立沖縄工業高等専門学校"
department = "メディア情報工学科4年"
mail = "info.aokabin@gmail.com"
tel = "090-1943-9827"
pic = Base64.encode64(File.new("qrcode.png").read)
back = Base64.encode64(File.new("back.png").read)

h = {"family" => family, "name" => name, "rubi_family" => rubi_family, "rubi_name" => rubi_name, "school" => school, "department" => department, "mail" => mail, "tel" => tel, "pic" => pic, "back" => back}

configure :production do
end

def make_card (hash)
  width = 637
  height = 385

  back_alpha = 0.5

  font_size_s = 20
  font_size_m = 25
  font_size_l = 72

  school_x = 220
  school_y = 50

  department_x = 300
  department_y = 90

  family_x = 180
  family_y = 220

  name_x = 380
  name_y = 220

  rubi_family_x = 220
  rubi_family_y = 150

  rubi_name_x = 400
  rubi_name_y = 150

  tel_x = 220
  tel_y = 350

  mail_x = 220
  mail_y = 380

  qrcode_x = 15
  qrcode_y = 240


  surface = Cairo::ImageSurface.new(width, height)
  context = Cairo::Context.new(surface)

  # 白背景
  context.set_source_rgb(1, 1, 1)  # 色指定=白色
  context.rectangle(0, 0, width, height)    # 画像サイズ分大きさを確保
  context.fill

  # 背景
  surface2 = Cairo::ImageSurface.from_png('back.png')
  context.set_source(surface2, 0, 0)   # 原点から画像を描画
  context.paint(back_alpha)

  # 学校名
  context.set_source_rgb(0, 0, 0)   # 色指定 = 黒色
  context.font_size = font_size_m
  context.move_to(school_x, school_y)
  context.show_text(hash["school"])

  # 所属名
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_m
  context.move_to(department_x, department_y)
  context.show_text(hash["department"])

  # 名字
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_l
  context.move_to(family_x, family_y)
  context.show_text(hash["family"])

  # 名前
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_l
  context.move_to(name_x, name_y)
  context.show_text(hash["name"])

  # 名前のルビ1
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_m
  context.move_to(rubi_family_x, rubi_family_y)
  context.show_text(hash["rubi_family"])

  # 名前のルビ2
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_m
  context.move_to(rubi_name_x, rubi_name_y)
  context.show_text(hash["rubi_name"])

  # 電話番号
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_s
  context.move_to(tel_x, tel_y)
  tel_str = "電話番号：" << hash["tel"]
  context.show_text(tel_str)

  # メールアドレス
  context.set_source_rgb(0, 0, 0)
  context.font_size = font_size_s
  context.move_to(mail_x, mail_y)
  mail_str = "メールアドレス：" << hash["mail"]
  context.show_text(mail_str)

  # QRコード
  surface2 = Cairo::ImageSurface.from_png('qrcode.png')
  context.set_source(surface2, qrcode_x, qrcode_y)
  context.paint

  surface.write_to_png('data.png')

  return Base64.encode64(File.new("data.png").read)
end

get "/" do
  erb :make
end

post "/make" do
  data = {"base64" => make_card(h)}
  data.to_json
end