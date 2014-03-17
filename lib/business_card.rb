require "tempfile"
require "cairo"
require "rmagick"

class BusinessCard
  class Base
    def initialize(x: self.class.const_get(:X), y: self.class.const_get(:Y))
      @x = x
      @y = y
    end
  end

  class Text < Base
    R = 0
    G = 0
    B = 0

    def initialize(
      x:    self.class.const_get(:X),
      y:    self.class.const_get(:Y),
      r:    self.class.const_get(:R),
      g:    self.class.const_get(:G),
      b:    self.class.const_get(:B),
      text: nil
    )
      super(x: x, y: y)
      @r    = r
      @g    = g
      @b    = b
      @text = text
    end

    def font_size
      @font_size ||= self.class.const_get(:FONT_SIZE)
    end

    def render(context)
      context.set_source_rgb(@r, @g, @b)
      context.font_size = font_size
      context.move_to(@x, @y)
      context.show_text(@text)
    end
  end

  class Image < Base
    def initialize(
      x:      self.class.const_get(:X),
      y:      self.class.const_get(:Y),
      width:  self.class.const_get(:WIDTH),
      height: self.class.const_get(:HEIGHT),
      alpha:  self.class.const_get(:ALPHA),
      path:   nil
    )
      super(x: x, y: y)
      @width  = width
      @height = height
      @alpha  = alpha
      @path   = path

      unless @path.nil?
        image = Magick::Image.read(@path).first
        image.resize!(@width, @height)
        image.write(@path)
      end
    end

    def render(context)
      unless @path.nil?
        context.set_source(Cairo::ImageSurface.from_png(@path), @x, @y)
        context.paint(@alpha)
      end
    end
  end

  class BackgroundImage < Image
    def initialize(
      x:      self.class.const_get(:X),
      y:      self.class.const_get(:Y),
      width:  self.class.const_get(:WIDTH),
      height: self.class.const_get(:HEIGHT),
      alpha:  self.class.const_get(:ALPHA),
      path:   nil,
      r:      self.class.const_get(:R),
      g:      self.class.const_get(:G),
      b:      self.class.const_get(:B)
    )
      super(x: x, y: y, width: width, height: height, alpha: alpha, path: path)
      @r = r
      @g = g
      @b = b
    end

    def render(context)
      if super.nil?
        context.set_source_rgb(@r, @g, @b)
        context.rectangle(@x, @y, @width, @height)
        context.fill
      end
    end
  end

  class Family < Text
    X         = 180
    Y         = 220
    FONT_SIZE = 72
  end

  class Name < Text
    X         = 380
    Y         = 220
    FONT_SIZE = 72
  end

  class RubiFamily < Text
    X         = 220
    Y         = 150
    FONT_SIZE = 25
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
    family:      nil,
    name:        nil,
    rubi_family: nil,
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
    @family.render(@context)
    @name.render(@context)
    @rubi_family.render(@context)
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
        "family",
        "name",
        "rubi_family",
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
