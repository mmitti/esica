require "tempfile"
require "cairo"
require "RMagick"

class BusinessCard
  class Base
    def x
      @x ||= self.class.const_get(:X)
    end

    def y
      @y ||= self.class.const_get(:Y)
    end
  end

  class Text < Base
    R = 0
    G = 0
    B = 0

    def initialize(text: nil)
      @text = text
    end

    def r
      @r ||= self.class.const_get(:R)
    end

    def g
      @g ||= self.class.const_get(:G)
    end

    def b
      @b ||= self.class.const_get(:B)
    end

    def font_size
      @font_size ||= self.class.const_get(:FONT_SIZE)
    end

    def render(context)
      context.set_source_rgb(self.r, self.g, self.b)
      context.font_size = font_size
      context.move_to(self.x, self.y)
      context.show_text(@text)
    end
  end

  class Image < Base
    def initialize(path: nil)
      @path = path

      unless @path.nil?
        image = Magick::Image.read(@path).first
        image.resize!(self.width, self.height)
        image.write(@path)
      end
    end

    def width
      @width ||= self.class.const_get(:WIDTH)
    end

    def height
      @height ||= self.class.const_get(:HEIGHT)
    end

    def alpha
      @alpha ||= self.class.const_get(:ALPHA)
    end

    def render(context)
      unless @path.nil?
        context.set_source(Cairo::ImageSurface.from_png(@path), self.x, self.y)
        context.paint(self.alpha)
      end
    end
  end

  class BackgroundImage < Image
    def r
      @r ||= self.class.const_get(:R)
    end

    def g
      @g ||= self.class.const_get(:G)
    end

    def b
      @b ||= self.class.const_get(:B)
    end

    def render(context)
      if super.nil?
        context.set_source_rgb(self.r, self.g, self.b)
        context.rectangle(self.x, self.y, self.width, self.height)
        context.fill
      end
    end
  end

  class Name < Text
    Y  = 220

    def x
      case @text.length - @text.count(' ')
      when 2
        330
      when 3
        260
      when 4
        220
      else
        190
      end
    end

    def font_size
      case @text.length - @text.count(' ')
      when 6
        65
      when 7
        55
      when 8
        50
      when 9
        45
      when 10
        40
      else
        72
      end
    end
  end

  class RubiName < Text
    X         = 400
    Y         = 150
    FONT_SIZE = 25
  end

  class School < Text
    X         = 220
    Y         = 50
    FONT_SIZE = 25
  end

  class Department < Text
    X         = 300
    Y         = 90
    FONT_SIZE = 25
  end

  class Mail < Text
    X         = 220
    Y         = 380
    FONT_SIZE = 20
  end

  class Tel < Text
    X         = 220
    Y         = 350
    FONT_SIZE = 20
  end

  class Pic < Image
    X      = 15
    Y      = 240
    WIDTH  = 95
    HEIGHT = 95
    ALPHA  = 1.0
  end

  class Back < BackgroundImage
    X      = 0
    Y      = 0
    WIDTH  = 637
    HEIGHT = 385
    ALPHA  = 0.5
    R      = 1
    G      = 1
    B      = 1
  end

  WIDTH     = 637
  HEIGHT    = 385

  FONT_FACE = "Osaka"

  def initialize(
    name:        nil,
    rubi_name:   nil,
    school:      nil,
    department:  nil,
    mail:        nil,
    tel:         nil,
    pic:         nil,
    back:        nil
  )
    self.class.keys.each do |key|
      raise(ArgumentError, "missing keyword: #{key}") if eval("#{key}.nil?")
      instance_variable_set(:"@#{key}", eval(key))
    end

    @surface = Cairo::ImageSurface.new(WIDTH, HEIGHT)
    @context = Cairo::Context.new(@surface)
    @context.select_font_face(FONT_FACE)
  end

  def make
    @back.render(@context)
    @school.render(@context)
    @department.render(@context)
    @name.render(@context)
    @rubi_name.render(@context)
    @tel.render(@context)
    @mail.render(@context)
    @pic.render(@context)

    @tempfile = Tempfile.new(["business_card", "png"])
    @surface.write_to_png(@tempfile.path)
    @tempfile.path
  end

  class << self
    def keys
      @keys ||= [
        "name",
        "rubi_name",
        "school",
        "department",
        "mail",
        "tel",
        "pic",
        "back"
      ]
    end
  end
end
