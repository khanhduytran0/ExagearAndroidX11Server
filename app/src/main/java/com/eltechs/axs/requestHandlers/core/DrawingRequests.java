package com.eltechs.axs.requestHandlers.core;

import com.eltechs.axs.geom.Rectangle;
import com.eltechs.axs.helpers.Assert;
import com.eltechs.axs.proto.input.XProtocolError;
import com.eltechs.axs.proto.input.annotations.Locks;
import com.eltechs.axs.proto.input.annotations.OOBParam;
import com.eltechs.axs.proto.input.annotations.RequestHandler;
import com.eltechs.axs.proto.input.annotations.RequestParam;
import com.eltechs.axs.proto.input.annotations.Width;
import com.eltechs.axs.proto.input.errors.BadMatch;
import com.eltechs.axs.proto.input.errors.BadValue;
import com.eltechs.axs.requestHandlers.HandlerObjectBase;
import com.eltechs.axs.requestHandlers.IncomingImageFormat;
import com.eltechs.axs.xconnectors.XResponse;
import com.eltechs.axs.xconnectors.XResponse.ResponseDataWriter;
import com.eltechs.axs.xserver.Drawable;
import com.eltechs.axs.xserver.GraphicsContext;
import com.eltechs.axs.xserver.Painter;
import com.eltechs.axs.xserver.Window;
import com.eltechs.axs.xserver.XServer;
import com.eltechs.axs.xserver.graphicsContext.PixelCompositionRule;
import java.io.IOException;
import java.nio.ByteBuffer;
import com.eltechs.axs.integersign.*;

public class DrawingRequests extends HandlerObjectBase {

    public enum CoordinateMode {
        ORIGIN,
        PREVIOUS
    }

    @RequestHandler(opcode = 67)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolyRectangle(@RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam ByteBuffer byteBuffer) {
    }

    @RequestHandler(opcode = 66)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolySegment(@RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam ByteBuffer byteBuffer) {
    }

    public DrawingRequests(XServer xServer) {
        super(xServer);
    }

    @RequestHandler(opcode = 72)
    @Locks({"DRAWABLES_MANAGER", "WINDOWS_MANAGER", "PIXMAPS_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
	@OOBParam(index = 0)
    public void PutImage(@RequestParam IncomingImageFormat incomingImageFormat, @RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam @Width(2) IntegerUnsigned i, @RequestParam @Width(2) IntegerUnsigned i2, @RequestParam @Width(2) IntegerSigned i3, @RequestParam @Width(2) IntegerSigned i4, @RequestParam byte b, @RequestParam byte b2, @RequestParam short s, @RequestParam ByteBuffer byteBuffer) throws XProtocolError {
        IncomingImageFormat incomingImageFormat2 = incomingImageFormat;
        byte b3 = b2;
        Painter painter = drawable.getPainter();
        if (!(graphicsContext.getFunction() == PixelCompositionRule.COPY || incomingImageFormat2 == IncomingImageFormat.Z_PIXMAP)) {
            Assert.notImplementedYet("Drawing with GC::Function values other than COPY is not supported yet.");
        }
        switch (incomingImageFormat2) {
            case BITMAP:
                if (b != 0) {
                    Assert.notImplementedYet("PutImage.leftPad != 0 not implemented yet");
                }
                if (b3 != 1) {
                    throw new BadMatch();
                }
                painter.drawBitmap(i3.value, i4.value, i.value, i2.value, byteBuffer);
                return;
            case XY_PIXMAP:
                if (drawable.getVisual().getDepth() != b3) {
                    throw new BadMatch();
                }
                return;
            case Z_PIXMAP:
                if (drawable.getVisual().getDepth() == b3 && b == 0) {
                    painter.drawZPixmap(graphicsContext.getFunction(), b3, i3.value, i4.value, 0, 0, i.value, i2.value, byteBuffer, i.value, i2.value);
                    return;
                }
                throw new BadMatch();
            default:
                Assert.state(false, String.format("Unknown IncomingImageFormat %s.", new Object[]{incomingImageFormat2}));
                return;
        }
    }

    @RequestHandler(opcode = 73)
    @Locks({"DRAWABLES_MANAGER", "PIXMAPS_MANAGER"})
	@OOBParam(index = 1)
    public void GetImage(XResponse xResponse, @RequestParam IncomingImageFormat incomingImageFormat, @RequestParam Drawable drawable, @RequestParam @Width(2) IntegerSigned i, @RequestParam @Width(2) IntegerSigned i2, @RequestParam @Width(2) IntegerUnsigned i3, @RequestParam @Width(2) IntegerUnsigned i4, @RequestParam int i5) throws XProtocolError, IOException {
        final int i6;
        if (incomingImageFormat == IncomingImageFormat.BITMAP) {
            throw new BadValue(incomingImageFormat.ordinal());
        }
        Rectangle rectangle = new Rectangle(i.value, i2.value, i3.value, i4.value);
        if (!(this.xServer.getPixmapsManager().getPixmap(drawable.getId()) != null)) {
            i6 = drawable.getVisual().getId();
        } else if (!new Rectangle(0, 0, drawable.getWidth(), drawable.getHeight()).containsInnerRectangle(rectangle)) {
            throw new BadMatch();
        } else {
            i6 = 0;
        }
        Painter painter = drawable.getPainter();
        byte[] bArr = null;
        switch (incomingImageFormat) {
            case XY_PIXMAP:
                Assert.notImplementedYet("Reading data as XY Pixmap is unimplemented yet.");
                break;
            case Z_PIXMAP:
                bArr = painter.getZPixmap(i.value, i2.value, i3.value, i4.value);
                break;
            default:
                Assert.state(false, String.format("Unknown IncomingImageFormat %s.", new Object[]{incomingImageFormat}));
                break;
        }
		final byte[] bArrPut = bArr;
        xResponse.sendSuccessReplyWithPayload((byte) drawable.getVisual().getDepth(), new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.putInt(i6);
            }
        }, bArr.length, new ResponseDataWriter() {
            public void write(ByteBuffer byteBuffer) {
                byteBuffer.put(bArrPut);
            }
        });
    }

    @RequestHandler(opcode = 62)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void CopyArea(@RequestParam Drawable drawable, @RequestParam Drawable drawable2, @RequestParam GraphicsContext graphicsContext, @RequestParam @Width(2) IntegerSigned i, @RequestParam @Width(2) IntegerSigned i2, @RequestParam @Width(2) IntegerSigned i3, @RequestParam @Width(2) IntegerSigned i4, @RequestParam @Width(2) IntegerUnsigned i5, @RequestParam @Width(2) IntegerUnsigned i6) {
        drawable2.getPainter().copyArea(graphicsContext, drawable, i.value, i2.value, i3.value, i4.value, i5.value, i6.value);
    }

    @RequestHandler(opcode = 61)
    @Locks({"WINDOWS_MANAGER"})
	@OOBParam(index = 0)
    public void ClearArea(@RequestParam Boolean bool, @RequestParam Window window, @RequestParam @Width(2) IntegerSigned i, @RequestParam @Width(2) IntegerSigned i2, @RequestParam @Width(2) IntegerUnsigned i3, @RequestParam @Width(2) IntegerUnsigned i4) {
        if (i3.value != 0 || i4.value != 0) {
            Assert.notImplementedYet("ClearArea is not implemented");
        }
    }

    @RequestHandler(opcode = 70)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
    public void PolyFillRectangle(@RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam ByteBuffer byteBuffer) {
        if (graphicsContext.getFunction() == PixelCompositionRule.COPY) {
            drawable.getPainter().drawFilledRectangles(byteBuffer, graphicsContext.getBackground());
        }
    }

    @RequestHandler(opcode = 65)
    @Locks({"DRAWABLES_MANAGER", "GRAPHICS_CONTEXTS_MANAGER"})
	@OOBParam(index = 0)
    public void PolyLine(@RequestParam CoordinateMode coordinateMode, @RequestParam Drawable drawable, @RequestParam GraphicsContext graphicsContext, @RequestParam ByteBuffer byteBuffer) {
        if (coordinateMode == CoordinateMode.ORIGIN && graphicsContext.getLineWidth() == 1 && graphicsContext.getFunction() == PixelCompositionRule.COPY) {
            drawable.getPainter().drawLines(byteBuffer, graphicsContext.getForeground(), graphicsContext.getLineWidth());
        }
    }
}
